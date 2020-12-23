package com.doobot.entities;

import net.dv8tion.jda.api.entities.User;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Team {
    private String name;
    private User captain;
    private List<String> members;
    private Date matchTime;
    private Match match;

    public Team(){
        name = "";
        captain = null;
    }

    public Team(String name, User captain, List<String> members){
        this.name = name;
        this.captain = captain;
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getCaptain() {
        return captain;
    }

    public void setCaptain(User captain) {
        this.captain = captain;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public void setMembers(String members){
        this.members = Arrays.asList(members.split(","));
    }

    public Date getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(Date matchTime) {
        this.matchTime = matchTime;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
}
