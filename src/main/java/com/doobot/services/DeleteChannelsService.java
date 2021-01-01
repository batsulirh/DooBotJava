package com.doobot.services;

import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;

import java.util.List;
import java.util.TimerTask;

public class DeleteChannelsService extends TimerTask {
    private Category category;

    public DeleteChannelsService(Category category){
        this.category = category;
    }

    @Override
    public void run() {
        List<GuildChannel> channelList = category.getChannels();
        for(GuildChannel channel : channelList){
            channel.delete().queue();
        }
        category.delete().queue();
    }

}
