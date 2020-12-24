package com.doobot.listeners;

import com.doobot.entities.Team;
import com.doobot.services.TeamService;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TeamListener extends ListenerAdapter {
    TeamService teamService;

    public TeamListener(){
        teamService = new TeamService();
    }
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User requestUser = event.getAuthor();
        Message msg = event.getMessage();
        MessageChannel msgChannel = msg.getChannel();

        if(msg.getContentDisplay().equals("!help")) {
            String helpMessage = teamService.GetHelpInfo(requestUser);
            msgChannel.sendMessage(helpMessage).queue();

        }else if(msg.getContentDisplay().startsWith("!setTeam")) {
            StringTokenizer tokenizer = new StringTokenizer(msg.getContentRaw(), " ");
            List<Member> mentionedMembers = new ArrayList<>();
            mentionedMembers.addAll(msg.getMentionedMembers());
            int i = 0;

            Member captain = mentionedMembers.get(0);
            mentionedMembers.remove(0);
            tokenizer.nextToken();
            String teamName = tokenizer.nextToken();
            Team team = new Team(teamName, captain, mentionedMembers);

            msgChannel.sendMessage("Team " + teamName + " has been created with the following roster: \nTeam Captain: " + team.getCaptain().getAsMention()).queue();
            for(Member member : team.getMembers()){
                i++;
                msgChannel.sendMessage("Member " + i + ": " + member.getAsMention()).queue();
            }

        }else if(msg.getContentDisplay().startsWith("!setMatch")) {

        }else if(msg.getContentDisplay().startsWith("!setTime")) {
            Date date = teamService.parseMatchTime(msg.getContentRaw());

            msgChannel.sendMessage("Your match time is now set to: " + date.toString() +
                    "\n" +
                    "When setting up the lobby for this series, please remember to set the team names to their respective team names.\n" +
                    "Please also be sure to capture replays for every match played. If a replay is not captured, then that match must be replayed.\n" +
                    "\n" +
                    "GLHF!").queue();

        }else if(msg.getContentDisplay().startsWith("!report"))  {

        }else if(msg.getContentDisplay().equals("!confirm"))     {

        }else if(msg.getContentDisplay().equals("!challenge"))   {

        }else if(msg.getContentDisplay().equals("!reset"))       {

        }
    }
}
