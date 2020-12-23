package com.doobot.entities;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Team {
    private String name;
    private String captain;
    private List<String> members;
    private Date matchTime;

    public Team(){
        name = "";
        captain = "";
    }

    public Team(String name, String captain, List<String> members){
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

    public String getCaptain() {
        return captain;
    }

    public void setCaptain(String captain) {
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
}
