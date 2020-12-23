package com.doobot.listeners;

import com.doobot.services.TeamService;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

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

        switch (msg.getContentDisplay()){
            case "!setTeam":
                break;
            case "!help":
                String helpMessage = teamService.GetHelpInfo(requestUser);
                msgChannel.sendMessage(helpMessage).queue();
            default:
                break;
        }
    }
}
