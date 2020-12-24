package com.doobot.entities;

import java.util.Date;

public class Match {
    private Team teamOne;
    private Team teamTwo;
    private int games;
    private Date matchTime;

    public Match(){
        teamOne = null;
        teamTwo = null;
        games = 0;
    }

    public Match(Team teamOne, Team teamTwo, int games){
        this.teamOne = teamOne;
        this.teamTwo = teamTwo;
        this.games = games;
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
}
