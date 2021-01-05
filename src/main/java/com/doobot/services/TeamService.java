package com.doobot.services;

import com.doobot.database.TeamsDB;
import com.doobot.entities.GameResult;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class TeamService {
    TeamsDB teamsDB;
    public TeamService(){
        teamsDB = new TeamsDB();
    }

    public String GetHelpInfo(Member requestUser){
        return String.format("Hello %s, You have access to the following commands: \n", requestUser.getUser().getName()) +
                "!setTime (mm/dd/yy) (AM/PM) - Sets the time for your next match \n" +
                "!report (Match1) (Team 1) (W/L) - Reports your match as complete with the passed result \n" +
                "!confirm - Allows you to confirm the result of the most recent result report \n" +
                "!challenge - Calls a tournament admin to challenge the recently reported match scoring\n" +
                "!reset - Resets the series score\n";
    }

    public Date parseMatchTime(String messageString) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mma", Locale.US);
            formatter.setTimeZone(TimeZone.getTimeZone("America/New_York"));

            Date date = formatter.parse(messageString);
            if(date.compareTo(formatter.parse(LocalDateTime.now().toString())) >= 1)
                return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String parseMembersToString(List<Member> members){
        StringBuilder memberString = new StringBuilder();
        for (Member member : members) {
            if(memberString.length() > 0)
                memberString.append(",");
            memberString.append(member.getId());
        }

        return memberString.toString();
    }

    public static List<String> parseMembersToList(String memberIds){
        List<String> membersList= new ArrayList<>();
        String[] splitMembers = memberIds.split(",");

        return membersList;
    }

    public static String parseGameResultsToString(List<GameResult> gameResults){
        if (gameResults == null)
            return "";

        StringBuilder resultString = new StringBuilder();
        for (GameResult result : gameResults) {
            if(result != null) {
                if(resultString.length() > 0)
                    resultString.append(",");
                resultString.append(result.getId());
            }
        }

        return resultString.toString();
    }

    public static List<Integer> parseGameResultsToList(String gameResultIds) {
        List<Integer> resultsList = new ArrayList<>();
        String[] splitMembers = gameResultIds.split(",");
        if(!gameResultIds.isEmpty()) {
            Arrays.stream(splitMembers).forEach(x -> resultsList.add(Integer.parseInt(x)));
        }
        return resultsList;
    }

    public String printContents(Message.Attachment attachment)
    {
        StringBuilder builder = new StringBuilder();
        attachment.retrieveInputStream().thenAccept(in -> {
            byte[] buf = new byte[1024];
            int count = 0;
            try {
                while ((count = in.read(buf)) > 0) {
                    builder.append(new String(buf, 0, count));
                }
                in.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).exceptionally(t -> { // handle failure
            t.printStackTrace();
            return null;
        });

        return builder.toString();
    }
}
