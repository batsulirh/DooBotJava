package com.doobot.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Match {
    private int id;
    private Team teamOne;
    private Team teamTwo;
    private int games;
    private Date matchTime;
    private boolean completed;
    private String categoryID;
    private List<GameResult> gameResults;
    private Team matchWinner;

    public Match(){
        this(null, null, 0, "");
    }

    public Match(Team teamOne, Team teamTwo, int games, String categoryID){
        this.teamOne = teamOne;
        this.teamTwo = teamTwo;
        this.games = games;
        this.categoryID = categoryID;
        completed = false;
        gameResults = new ArrayList<>();
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

    public List<GameResult> getGameResults() {
        return gameResults;
    }

    public void addGameResult(GameResult result) {
        this.gameResults.add(result);
    }

    public void setGameResults(List<GameResult> gameResults) {
        this.gameResults = gameResults;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public Team getMatchWinner() {
        return matchWinner;
    }

    public void setMatchWinner(Team matchWinner) {
        this.matchWinner = matchWinner;
    }
}
