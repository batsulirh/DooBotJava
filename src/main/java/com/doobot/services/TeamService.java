package com.doobot.services;

import com.doobot.database.TeamsDB;
import net.dv8tion.jda.api.entities.User;

public class TeamService {
    TeamsDB teamsDB;
    public TeamService(){
        teamsDB = new TeamsDB();
    }

    public String GetHelpInfo(User requestUser){
        return String.format("Hello {0}, You have access to the following commands: \n", requestUser.getName()) +
                "!setTime (mm/dd/yy) (AM/PM) - Sets the time for your next match \n" +
                "!report (Match1) (Team 1) (W/L) - Reports your match as complete with the passed result \n" +
                "!confirm - Allows you to confirm the result of the most recent result report \n" +
                "!challenge - Calls a tournament admin to challenge the recently reported match scoring\n" +
                "!reset - Resets the series score\n";
    }
}
