package com.doobot.database;

import com.doobot.entities.Match;
import com.doobot.entities.Team;
import com.doobot.services.TeamService;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.sqlite.SQLiteErrorCode;
import org.sqlite.SQLiteException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
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
            StringBuilder memberString = new StringBuilder();
            for (Member member : team.getMembers()) {
                memberString.append(",").append(member.getUser().getName());
            }
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

    public Team GetTeam() {

        return null;
    }

    public void EditTeam(Team team){

    }

    public void DeleteTeam(Team team) {

    }

    public Match GetMatch() {
        return null;
    }

    public void EditMatch(Match match){

    }

    public void DeleteMatch(Match match) {

    }

    private String CreateTeamsTable(){
        return """
                CREATE TABLE IF NOT EXISTS teams (
                id integer NOT NULL PRIMARY KEY,\s
                name text NOT NULL UNIQUE,\s
                captainid text NOT NULL,\s
                memberids text NOT NULL,\s
                matchtime text\s
                );""";
    }

    private String CreateMatchesTable(){
        return """
                CREATE TABLE IF NOT EXISTS matches (
                id integer NOT NULL PRIMARY KEY,\s
                teamoneid integer NOT NULL, \s
                teamtwoid integer NOT NULL, \s
                games integer, \s
                matchtime text \s 
                );
                """;
    }
}
