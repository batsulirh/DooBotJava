package com.doobot.database;

import com.doobot.entities.Team;

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
            System.out.println("DB retrieved successfully");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void AddNewTeam(String teamName, String captain, String membersCommaSplit, String matchTime){
        List<String> members = Arrays.asList(membersCommaSplit.split(","));
        Date matchTimeAsDate = null;
        try {
            matchTimeAsDate = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS.SSS").parse(matchTime);
            Team team = new Team(teamName, captain, members);
            team.setMatchTime(matchTimeAsDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Team GetTeam() {
        return null;
    }

    public void EditTeam(Team team){

    }

    public void DeleteTeam() {

    }

    private String CreateTeamsTable(){
        return """
                CREATE TABLE IF NOT EXISTS teams (
                id integer PRIMARY KEY\s
                name text NOT NULL UNIQUE\s
                captain text NOT NULL\s
                members text NOT NULL\s
                matchtime text\s
                );""";
    }
}
