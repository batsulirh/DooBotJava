package com.doobot.listeners;

import com.doobot.services.TeamService;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.TimeZone;

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
