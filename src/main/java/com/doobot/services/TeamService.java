package com.doobot.services;

import net.dv8tion.jda.api.entities.User;

import java.text.MessageFormat;

public class TeamService {

    public String GetHelpInfo(User requestUser){
        StringBuilder helpString = new StringBuilder();
        helpString.append(String.format( "Hello {0}, You have access to the following commands: \n", requestUser.getName()));
        helpString.append("!setTime (mm/dd/yy) (AM/PM) - Sets the time for your next match \n");
        helpString.append("!report (Match1) (Team 1) (W/L) - Reports your match as complete with the passed result \n");
        helpString.append("!confirm - Allows you to confirm the result of the most recent result report \n");
        helpString.append("!challenge - Calls a tournament admin to challenge the recently reported match scoring\n");
        helpString.append("!reset - Resets the series score\n");
        return helpString.toString();
    }
}
