package com.doobot.entities;

public class GameResult {
    private int id;
    private int matchId;
    private Team winningTeam;
    private String replayStream;
    private String replayFileExtension;
    private String replayFileName;

    public GameResult(){
        this(0, null, "", "", "");
    }

    public GameResult(int matchId, Team winningTeam, String replayStream, String replayFileExtension, String replayFileName){
        this.matchId = matchId;
        this.winningTeam = winningTeam;
        this.replayStream = replayStream;
        this.replayFileExtension = replayFileExtension;
        this.replayFileName = replayFileName;
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

    public String getReplayStream() {
        return replayStream;
    }

    public void setReplayStream(String replayStream) {
        this.replayStream = replayStream;
    }

    public String getReplayFileExtension() {
        return replayFileExtension;
    }

    public void setReplayFileExtension(String replayFileExtension) {
        this.replayFileExtension = replayFileExtension;
    }

    public String getReplayFileName() {
        return replayFileName;
    }

    public void setReplayFileName(String replayFileName) {
        this.replayFileName = replayFileName;
    }
}
