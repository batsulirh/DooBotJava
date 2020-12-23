package com.doobot.services;

import com.doobot.database.TeamsDB;
import net.dv8tion.jda.api.entities.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;

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

    public Date parseMatchTime(String messageString){

        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy hh:mm a", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("America/New_York"));

        StringTokenizer tokens = new StringTokenizer(messageString, " ");
        Date date;
        if(tokens.countTokens() == 4) {
            try {
                tokens.nextToken();
                String dateString = tokens.nextToken();
                String timeString = tokens.nextToken();
                String periodString = tokens.nextToken();

                String concatenatedString = dateString +  " " + timeString + " " + periodString;
                date = formatter.parse(concatenatedString);

                return date;
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
