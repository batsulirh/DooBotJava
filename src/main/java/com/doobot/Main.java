package com.doobot;

import com.doobot.listeners.TeamListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class Main extends ListenerAdapter {
    public static void main(String[] args) {
        String botToken = "";
        try {
            Path paths = Paths.get("./src/main/resources/token.txt").toRealPath();
            botToken = new String(Files.readAllBytes(paths));

            JDA jda = JDABuilder.createDefault(botToken, EnumSet.allOf(GatewayIntent.class))
                    .setActivity(Activity.playing("The Management Game"))
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .addEventListeners(new TeamListener())
                    .build();
            jda.awaitReady();
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}