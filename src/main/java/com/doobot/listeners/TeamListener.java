package com.doobot.listeners;

import com.doobot.database.TeamsDB;
import com.doobot.entities.Match;
import com.doobot.entities.Team;
import com.doobot.services.TeamService;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class TeamListener extends ListenerAdapter {
    TeamService teamService;
    TeamsDB teamsDB;
    public TeamListener(){
        teamService = new TeamService();
        teamsDB = new TeamsDB();

    }
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Member requestUser = event.getMember();
        Message msg = event.getMessage();
        MessageChannel msgChannel = msg.getChannel();
        Guild guild = event.getGuild();


        if(event.getAuthor().isBot()){
            return;
        }else if(msg.getContentDisplay().equals("!help")) {
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

            String result = teamsDB.AddNewTeam(team);
            if(result == "") {
                msgChannel.sendMessage("Team " + teamName + " has been created with the following roster: \nTeam Captain: " + team.getCaptain().getAsMention()).queue();
                for(Member member : team.getMembers()) {
                    i++;
                    msgChannel.sendMessage("Member " + i + ": " + member.getAsMention()).queue();
                }
            }
            else {
                msgChannel.sendMessage(result).queue();
            }


        }else if(msg.getContentDisplay().startsWith("!setMatch")) {
            String[] requestedTeams = msg.getContentRaw().split(" ");
            Team team1 = teamsDB.GetTeam(requestedTeams[1], event.getGuild());
            Team team2 = teamsDB.GetTeam(requestedTeams[2], event.getGuild());

            Team bothTeams = new Team();
            bothTeams.addMembers(team1.getMembers());
            bothTeams.addMembers(team2.getMembers());

            teamsDB.AddMatch(new Match(team1, team2, Integer.parseInt(requestedTeams[3])));

            try {
                Category category = guild.createCategory(team1.getName() + " vs " + team2.getName()).submit().get();
                TextChannel textChannel = category.createTextChannel("match-schedule").submit().get();
                VoiceChannel team1Voice = category.createVoiceChannel(team1.getName()).submit().get();
                VoiceChannel team2Voice = category.createVoiceChannel(team2.getName()).submit().get();

                category.createPermissionOverride(guild.getPublicRole()).setDeny(Permission.ALL_PERMISSIONS).queue();

                for(Member member: bothTeams.getMembers()){
                    textChannel.createPermissionOverride(member).setAllow(Permission.VIEW_CHANNEL).setAllow(Permission.MESSAGE_HISTORY).setAllow(Permission.MESSAGE_WRITE).queue();
                }

                team1Voice.createPermissionOverride(team2.getCaptain()).setDeny(Permission.VOICE_CONNECT).queue();
                team2Voice.createPermissionOverride(team1.getCaptain()).setDeny(Permission.VOICE_CONNECT).queue();

                for(Member member: team1.getMembers()){
                    team1Voice.createPermissionOverride(member).setAllow(Permission.VOICE_CONNECT).queue();
                    team2Voice.createPermissionOverride(member).setDeny(Permission.VOICE_CONNECT).queue();
                }

                for(Member member: team2.getMembers()){
                    team2Voice.createPermissionOverride(member).setAllow(Permission.VOICE_CONNECT).queue();
                    team1Voice.createPermissionOverride(member).setDeny(Permission.VOICE_CONNECT).queue();
                }

            } catch (InterruptedException e) {
                msgChannel.sendMessage("Oops! There was an error on our end, please try again").queue();
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }


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
            //Needs Admin channel ID for implementation

            List<Role> roles = msg.getMember().getRoles();

            for(Role role: roles){
                if(role.getName().equals( "Team Captain")){
                    guild.getTextChannelById("admin ChannelID").sendMessage("A match in " + msg.getJumpUrl() +  " has an issue!").queue();
                }else{
                    msgChannel.sendMessage("Sorry, but you need to be a Team Captain to raise an issue about a match. Please ask your captain to call an admin with \"!challenge\"").queue();
                }
            }

        }else if(msg.getContentDisplay().equals("!reset"))       {

        }
    }
}
