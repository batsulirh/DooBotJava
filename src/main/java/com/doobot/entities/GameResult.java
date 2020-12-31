package com.doobot.entities;

public class GameResult {
    private int id;
    private int matchId;
    private Team winningTeam;
    private String replay;

    public GameResult(){
        this(0, null);
    }

    public GameResult(int matchId, Team winningTeam){
        this.matchId = matchId;
        this.winningTeam = winningTeam;
        this.replay = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public Team getWinningTeam() {
        return winningTeam;
    }

    public void setWinningTeam(Team winningTeam) {
        this.winningTeam = winningTeam;
    }

    public String getReplay() {
        return replay;
    }

    public void setReplay(String replay) {
        this.replay = replay;
    }
}
