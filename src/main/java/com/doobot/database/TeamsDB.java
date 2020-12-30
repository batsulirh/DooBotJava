package com.doobot.database;

import com.doobot.entities.Match;
import com.doobot.entities.Team;
import com.doobot.services.TeamService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.sqlite.SQLiteErrorCode;
import org.sqlite.SQLiteException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeamsDB {

    private static Connection teamsConn = null;

    public TeamsDB(){
        InitializeDatabase();
    }

    public void InitializeDatabase(){
        try {
            if (teamsConn == null){
                Path paths = Paths.get("./src/main/java/com/doobot/database").toRealPath();
                String url = "jdbc:sqlite:"+paths.toString()+"\\teams.db";
                teamsConn = DriverManager.getConnection(url);
            }
            Statement stmt = teamsConn.createStatement();
            stmt.execute(CreateTeamsTable());
            stmt.execute(CreateMatchesTable());
            System.out.println("DB retrieved successfully");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public String AddNewTeam(Team team) {
        String errorString = "";
        try {
            String sql = String.format("""
                    INSERT INTO teams (name, captainid, memberids)\s
                    values('%s','%s','%s');
                    """, team.getName(), team.getCaptain().getId(), TeamService.parseMembersToString(team.getMembers()));

            Statement stmt = teamsConn.createStatement();
            stmt.execute(sql);
        } catch (SQLiteException ex){
            if (ex.getResultCode() == SQLiteErrorCode.SQLITE_CONSTRAINT_UNIQUE)
                errorString = String.format("Team %s already exists. Please use a unique team name", team.getName());
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return errorString;
    }

    public Team GetTeam(String teamName, Guild guild) {
        ResultSet results;
        Team team = null;
        String sql = String.format("""
                SELECT id, name, captainid, memberids
                from teams
                where name = '%s';
                """, teamName);
        try {
            PreparedStatement stmt = teamsConn.prepareStatement(sql);
            results = stmt.executeQuery();
            if(results.next()) {
                Member captain = guild.getMemberById(results.getString("captainid"));
                List<String> memberStrings = TeamService.parseMembersToList(results.getString("memberids"));
                List<Member> memberList = new ArrayList<>();
                memberStrings.forEach(m -> memberList.add(guild.getMemberById(m)));
                team = new Team(results.getString("name"), captain, memberList);
                team.setId(results.getInt("id"));

                return team;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return team;
    }

    public String EditTeam(Team team){
        String errorString = "";
        String members = TeamService.parseMembersToString(team.getMembers());
        String sql = String.format("""
                UPDATE teams SET
                    name = %s,\s
                    captainid = %s,\s
                    memberids = %s\s
                WHERE id = %x;
                """, team.getName(), team.getCaptain().getId(), members, team.getId());
        try {
            Statement stmt = teamsConn.createStatement();
            stmt.execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return errorString;
    }

    public void DeleteTeam(Team team) {

    }

    public String AddMatch(Match match){
        String matchTime = match.getMatchTime() == null ? "" : match.getMatchTime().toString();
        String sql = String.format("""
                INSERT INTO matches (teamoneid, teamtwoid, games, matchtime, categoryid, completed, results) VALUES
                ('%s', '%s', %x, datetime('%s'), '%s', '%s', '%s');
                """,
                match.getTeamOne().getId(), match.getTeamTwo().getId(), match.getGames(),
                matchTime, match.getCategoryID(), match.isCompleted(), match.getResults());
        try {
            Statement stmt = teamsConn.createStatement();
            stmt.execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "";
    }

    public Match GetMatch(Team teamone, Team teamtwo) {
        ResultSet results;
        String sql = String.format("""
                SELECT * FROM matches \s
                WHERE 
                    (teamoneid = %x AND teamtwoid = %x)
                OR 
                    (teamoneid = %x AND teamtwoid = %x);
                """, teamone.getId(), teamtwo.getId(),
                teamtwo.getId(), teamone.getId());

        try{
            PreparedStatement stmt = teamsConn.prepareStatement(sql);
            results = stmt.executeQuery();
            Match match = null;
            if(results.next()){
                match = new Match(teamone, teamtwo, results.getInt("games"), results.getString("categoryid"));
                match.setCompleted(results.getBoolean("completed"));
                match.setMatchTime(results.getDate("matchtime"));
                match.setResults(results.getString("results"));
            }
            return match;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public String EditMatch(Match match){
        String errorString = "";
        String sql = String.format("""
                UPDATE matches SET
                    teamoneid = '%s',\s
                    teamtwoid = '%s',\s
                    games = %x,\s
                    matchtime = datetime('%s'),\s
                    categoryid = '%s', \s 
                    completed = '%s', \s
                    results = '%s' \s
                WHERE id = %x;
                """, match.getTeamOne().getId(),
                match.getTeamTwo().getId(),
                match.getGames(),
                match.getMatchTime(),
                match.getCategoryID(),
                match.isCompleted(),
                match.getResults(),
                match.getId());
        try {
            Statement stmt = teamsConn.createStatement();
            stmt.execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return errorString;
    }

    public void DeleteMatch(Match match) {

    }

    private String CreateTeamsTable(){
        return """
                CREATE TABLE IF NOT EXISTS teams (
                id integer NOT NULL PRIMARY KEY,\s
                name text NOT NULL UNIQUE,\s
                captainid text NOT NULL,\s
                memberids text NOT NULL\s
                );""";
    }

    private String CreateMatchesTable(){
        return """
                CREATE TABLE IF NOT EXISTS matches (
                id integer NOT NULL PRIMARY KEY,\s
                teamoneid integer NOT NULL, \s
                teamtwoid integer NOT NULL, \s
                games integer, \s
                matchtime date, \s
                categoryid text, \s 
                completed boolean DEFAULT false, \s
                results text \s
                );
                """;
    }
}
