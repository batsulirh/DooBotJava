package com.doobot.entities;

import java.util.Date;

public class Match {
    private int id;
    private Team teamOne;
    private Team teamTwo;
    private int games;
    private Date matchTime;
    private boolean completed;
    private String results;

    public Match(){
        this(null, null, 0);
    }

    public Match(Team teamOne, Team teamTwo, int games){
        this.teamOne = teamOne;
        this.teamTwo = teamTwo;
        this.games = games;
        completed = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Team getTeamOne() {
        return teamOne;
    }

    public void setTeamOne(Team teamOne) {
        this.teamOne = teamOne;
    }

    public Team getTeamTwo() {
        return teamTwo;
    }

    public void setTeamTwo(Team teamTwo) {
        this.teamTwo = teamTwo;
    }

    public int getGames() {
        return games;
    }

    public void setGames(int games) {
        this.games = games;
    }

    public Date getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(Date matchTime) {
        this.matchTime = matchTime;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }
}
