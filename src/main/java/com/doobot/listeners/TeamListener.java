package com.doobot.listeners;

import com.doobot.database.TeamsDB;
import com.doobot.entities.GameResult;
import com.doobot.entities.Match;
import com.doobot.entities.Team;
import com.doobot.services.TeamService;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.lang.reflect.Array;
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


        if (event.getAuthor().isBot()) {

        } else if (msg.getContentDisplay().equals("!help")) {
            String helpMessage = teamService.GetHelpInfo(requestUser);
            msgChannel.sendMessage(helpMessage).queue();

        } else if (msg.getContentDisplay().startsWith("!setTeam")) {
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
            if (result.equals("")) {
                msgChannel.sendMessage("Team " + teamName + " has been created with the following roster: \nTeam Captain: " + team.getCaptain().getAsMention()).queue();
                for (Member member : team.getMembers()) {
                    i++;
                    msgChannel.sendMessage("Member " + i + ": " + member.getAsMention()).queue();
                }
            } else {
                msgChannel.sendMessage(result).queue();
            }


        } else if (msg.getContentDisplay().startsWith("!setMatch")) {

            try {
                String[] requestedTeams = msg.getContentRaw().split(" ");
                Team team1 = teamsDB.GetTeamByName(requestedTeams[1], event.getGuild());
                Team team2 = teamsDB.GetTeamByName(requestedTeams[2], event.getGuild());

                Team bothTeams = new Team();
                bothTeams.addMembers(team1.getMembers());
                bothTeams.addMembers(team2.getMembers());

                //Channel and category creation
                Category category = guild.createCategory(team1.getName() + " vs " + team2.getName()).submit().get();
                TextChannel textChannel = category.createTextChannel("match-schedule").submit().get();
                VoiceChannel team1Voice = category.createVoiceChannel(team1.getName()).submit().get();
                VoiceChannel team2Voice = category.createVoiceChannel(team2.getName()).submit().get();

                //Constants for Permissions
                List<Permission> textChannelPerms = new ArrayList<Permission>(Arrays.asList(Permission.MESSAGE_WRITE, Permission.MESSAGE_HISTORY, Permission.VIEW_CHANNEL, Permission.MESSAGE_ADD_REACTION));
                List<Permission> voiceChannelPerms = new ArrayList<>(Arrays.asList(Permission.VOICE_CONNECT, Permission.VIEW_CHANNEL, Permission.VOICE_SPEAK, Permission.VOICE_STREAM, Permission.VOICE_USE_VAD));
                List<Permission> voiceChannelPermsOtherTeam = new ArrayList<>(Arrays.asList(Permission.VIEW_CHANNEL));
                List<Permission> voiceChannelPermsOtherTeamDeny = new ArrayList<>(Arrays.asList(Permission.VOICE_CONNECT));

                //Permissions editing for category and created channels
                category.createPermissionOverride(guild.getPublicRole()).setDeny(Permission.ALL_PERMISSIONS).queue();

                category.createPermissionOverride(team1.getCaptain()).setPermissions(textChannelPerms, null).queue();
                category.createPermissionOverride(team2.getCaptain()).setPermissions(textChannelPerms, null).queue();

                for(Member member: bothTeams.getMembers()){
                    textChannel.createPermissionOverride(member).setPermissions(textChannelPerms, null).queue();
                }

                textChannel.createPermissionOverride(team1.getCaptain()).setPermissions(textChannelPerms, null).queue();
                textChannel.createPermissionOverride(team2.getCaptain()).setPermissions(textChannelPerms, null).queue();

                team1Voice.createPermissionOverride(team2.getCaptain()).setPermissions(voiceChannelPermsOtherTeam, voiceChannelPermsOtherTeamDeny).queue();
                team2Voice.createPermissionOverride(team2.getCaptain()).setPermissions(voiceChannelPerms, null).queue();

                team1Voice.createPermissionOverride(team1.getCaptain()).setPermissions(voiceChannelPerms,  null).queue();
                team2Voice.createPermissionOverride(team1.getCaptain()).setPermissions(voiceChannelPermsOtherTeam, voiceChannelPermsOtherTeamDeny).queue();

                for (Member member : team1.getMembers()) {
                    team1Voice.createPermissionOverride(member).setPermissions(voiceChannelPerms, null).queue();
                    team2Voice.createPermissionOverride(member).setPermissions(voiceChannelPermsOtherTeam, voiceChannelPermsOtherTeamDeny).queue();
                }

                for (Member member : team2.getMembers()) {
                    team2Voice.createPermissionOverride(member).setPermissions(voiceChannelPerms, null).queue();
                    team1Voice.createPermissionOverride(member).setPermissions(voiceChannelPermsOtherTeam,voiceChannelPermsOtherTeamDeny).queue();
                }

                //Match DB storage and creation
                String result = teamsDB.AddMatch(new Match(team1, team2, Integer.parseInt(requestedTeams[3]), category.getId()));

                if (!result.equals("")) {
                    msgChannel.sendMessage("Issue creating a new match: " + result).queue();
                    return;
                }

                Match createdMatch = teamsDB.GetMatchByTeams(team1, team2, guild);
                textChannel.sendMessage(String.format("Match %x: %s vs %s with best of %x game(s) successfully created!",
                        createdMatch.getId(),
                        createdMatch.getTeamOne().getName(), createdMatch.getTeamTwo().getName(),
                        createdMatch.getGames())).queue();

            } catch (InterruptedException e) {
                msgChannel.sendMessage("Oops! There was an error on our end, please try again").queue();
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
                msgChannel.sendMessage("Oops! There was an error on our end, please try again").queue();
            }


        } else if (msg.getContentDisplay().startsWith("!setTime")) {
            String[] requestedTime = msg.getContentRaw().split(" ");
            Date date = teamService.parseMatchTime(requestedTime[1]);

            // teamsDB.GetMatch()
            // teamsDB.EditMatch()
            msgChannel.sendMessage("Your match time is now set to: " + date.toString() +
                    "\n" +
                    "When setting up the lobby for this series, please remember to set the team names to their respective team names.\n" +
                    "Please also be sure to capture replays for every match played. If a replay is not captured, then that match must be replayed.\n" +
                    "\n" +
                    "GLHF!").queue();

        } else if (msg.getContentDisplay().startsWith("!report")) {
            String[] splitString = msg.getContentRaw().split(" ");

            if(msg.getAttachments().isEmpty()){
                msgChannel.sendMessage("Sorry! Reported games must include a replay attachment.").queue();

            }else if(msg.getAttachments().get(0).getFileExtension().equals("replay")){
                Match currentMatch = teamsDB.GetMatchByCategoryId(msg.getCategory().getId(), guild);
                List<Team> teamsInMatch = new ArrayList<>(Arrays.asList(currentMatch.getTeamOne(), currentMatch.getTeamTwo()));
                Team winningTeam = teamsDB.GetTeamByName(splitString[1], guild);

                if (teamsInMatch.get(0).getId() == winningTeam.getId() || teamsInMatch.get(1).getId() == winningTeam.getId()){
                    Message.Attachment passedAttachment = msg.getAttachments().get(0);
                    String attachmentString = teamService.printContents(passedAttachment);
                    GameResult gameResult = new GameResult(currentMatch.getId(), winningTeam,
                            attachmentString, passedAttachment.getFileExtension(), passedAttachment.getFileName());

                    teamsDB.AddGameResult(gameResult);
                    List<GameResult> gameResultsList= teamsDB.GetGameResultsByMatchId(currentMatch.getId(), guild);
                    currentMatch.setGameResults(gameResultsList);
                    teamsDB.EditMatch(currentMatch);


                    int threshold =  (currentMatch.getGames()/2) + 1;
                    int teamOneWins = 0;
                    int teamTwoWins = 0;
                    for(GameResult result : currentMatch.getGameResults()){
                        if(result.getWinningTeam().getId() == currentMatch.getTeamOne().getId()){
                            teamOneWins++;
                        }else if(result.getWinningTeam().getId() == currentMatch.getTeamTwo().getId()){
                            teamTwoWins++;
                        }
                    }
                    if(teamOneWins >= threshold){
                        currentMatch.setMatchWinner(currentMatch.getTeamOne());
                        msgChannel.sendMessage("Team " + currentMatch.getMatchWinner().getName() + " has won the necessary amount of games to win the series! Waiting on the other team captain to confirm. ").queue();
                    }else if(teamTwoWins >= threshold){
                        currentMatch.setMatchWinner(currentMatch.getTeamTwo());
                        msgChannel.sendMessage("Team " + currentMatch.getMatchWinner().getName() + " has won the necessary amount of games to win the series! Waiting on the other team captain to confirm. ").queue();
                    }else if(currentMatch.getGameResults().size() >= currentMatch.getGames()){
                        currentMatch.setMatchWinner(null);
                    }else{
                        msg.addReaction("U+1F44C").queue();
                    }
                }else{
                    msgChannel.sendMessage("Sorry, I don't think that team is playing in this match! ").queue();
                    return;
                }
            }
        } else if (msg.getContentDisplay().equals("!confirm")) {


        } else if (msg.getContentDisplay().equals("!challenge")) {
            //Needs Admin channel ID for implementation

            List<Role> roles = msg.getMember().getRoles();

            for (Role role : roles) {
                if (role.getName().equals("Team Captain")) {
                    guild.getTextChannelById("admin ChannelID").sendMessage("A match in " + msg.getJumpUrl() + " has an issue!").queue();
                } else {
                    msgChannel.sendMessage("Sorry, but you need to be a Team Captain to raise an issue about a match. Please ask your captain to call an admin with \"!challenge\"").queue();
                }
            }

        } else if (msg.getContentDisplay().equals("!reset")) {

        } else if (msg.getContentDisplay().equals("!listTeams")) {


        } else if (msg.getContentDisplay().equals("!listMatches")) {

        } else if (msg.getContentDisplay().equals("exportTeams")) {

        } else if(msg.getContentDisplay().equals("exportMatches")) {

        }
    }
}
