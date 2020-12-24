package com.doobot.entities;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Team {
    private String name;
    private Member captain;
    private List<Member> members;

    public Team(){
        name = "";
        captain = null;
    }

    public Team(String name, Member captain, List<Member> members){
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

    public Member getCaptain() {
        return captain;
    }

    public void setCaptain(Member captain) {
        this.captain = captain;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public void addMember(Member member){
        this.members.add(member);
    }
}
