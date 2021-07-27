package com.oakleyplugins.discordintegration;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.awt.*;
import java.time.Instant;

public class Methods {
    //All of these are a mess need to find a better way to do this....
    public static MessageEmbed embedFromPlayer(Color color, Player player, String title){
        title = ChatColor.stripColor(title);
        String name = player.getName();
        String uuid = player.getUniqueId().toString();
        EmbedBuilder embed = new EmbedBuilder()
                .setColor(color)
                .setAuthor(name,null,"https://crafatar.com/renders/head/" + uuid + "?size=128&default=MHF_Steve&overlay")
                .setTitle(title)
                .setTimestamp(Instant.now());
        return embed.build();
    }


    public static MessageEmbed embedFromPlayer(Color color, Player player, String title, MessageEmbed.Field... fields){
        title = ChatColor.stripColor(title);
        String name = player.getName();
        String uuid = player.getUniqueId().toString();
        EmbedBuilder embed = new EmbedBuilder()
                .setColor(color)
                .setAuthor(name,null,"https://crafatar.com/renders/head/" + uuid + "?size=128&default=MHF_Steve&overlay")
                .setTitle(title)
                .setTimestamp(Instant.now());
        for(MessageEmbed.Field field : fields){
            embed.addField(field);
        }
        return embed.build();
    }


    public static MessageEmbed embedFromCustom(Color color, String authorName, String iconURL, String title, String description){
        title = ChatColor.stripColor(title);
        EmbedBuilder embed = new EmbedBuilder()
                .setColor(color)
                .setAuthor(authorName,null, iconURL)
                .setTitle(title)
                .setDescription(description)
                .setTimestamp(Instant.now());
        return embed.build();
    }

    public static MessageEmbed embedFromCustom(Color color,  String title, String description){
        title = ChatColor.stripColor(title);
        EmbedBuilder embed = new EmbedBuilder()
                .setColor(color)
                .setTitle(title)
                .setDescription(description)
                .setTimestamp(Instant.now());
        return embed.build();
    }



    public static MessageEmbed embedFromCustom(Color color, String authorName, String iconURL, String title, MessageEmbed.Field... fields){
        title = ChatColor.stripColor(title);
        EmbedBuilder embed = new EmbedBuilder()
                .setColor(color)
                .setAuthor(authorName,null, iconURL)
                .setTitle(title)
                .setTimestamp(Instant.now());
        for(MessageEmbed.Field field : fields){
            embed.addField(field);
        }
        return embed.build();
    }


    public static String formatSeconds(double timeInSeconds) {
        int hours = (int)timeInSeconds / 3600;
        double secondsLeft = timeInSeconds - hours * 3600;
        int minutes = (int) secondsLeft / 60;
        double seconds = secondsLeft - minutes * 60;
        seconds = (Math.round(seconds * 100.0) / 100.0);

        String formattedTime = "";
        if (hours >= 1)
            formattedTime += hours + "Hour(s) ";

        if (minutes >= 1)
            formattedTime += minutes + "Minute(s) ";
        else if(hours >= 1) formattedTime += minutes + "Minute(s) ";

        formattedTime += seconds + "Second(s)";

        return formattedTime;
    }
}
