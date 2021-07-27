package com.oakleyplugins.discordintegration.Events;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.BroadcastMessageEvent;
import org.bukkit.event.server.ServerCommandEvent;

import static com.oakleyplugins.discordintegration.DiscordIntegration.*;
import static com.oakleyplugins.discordintegration.Methods.*;

import java.awt.*;

public class MCEvents implements Listener {

    @EventHandler
    public void onSay(ServerCommandEvent e){
        if(e.getCommand().startsWith("say ") & jda.getStatus().equals(JDA.Status.CONNECTED)){
            MessageEmbed embed = embedFromCustom(Color.YELLOW, "Server", "https://i.ibb.co/SrTVRYY/2e4c6838faaf4dfc816a7801c032ff32.png", e.getCommand().substring(4));
            channel.sendMessageEmbeds(embed).queue();
        }
    }

    @EventHandler
    public void onBroadcast(BroadcastMessageEvent e){
        if(jda.getStatus().equals(JDA.Status.CONNECTED)){
            MessageEmbed embed = embedFromCustom(Color.YELLOW, "Server", "https://i.ibb.co/SrTVRYY/2e4c6838faaf4dfc816a7801c032ff32.png", e.getMessage());
            channel.sendMessageEmbeds(embed).queue();
        }
    }

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent e){
        if(jda.getStatus().equals(JDA.Status.CONNECTED)){
            MessageEmbed embed = embedFromPlayer(Color.WHITE, e.getPlayer(), e.getMessage());
            channel.sendMessageEmbeds(embed).queue();
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        if(jda.getStatus().equals(JDA.Status.CONNECTED)){
            Player p = e.getEntity();
            String stats = formatSeconds((double) p.getStatistic(Statistic.TIME_SINCE_DEATH)/20);
            MessageEmbed embed = embedFromPlayer(Color.RED, p, e.getDeathMessage(),new MessageEmbed.Field("Survived:", stats, false));
            channel.sendMessageEmbeds(embed).queue();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        if(jda.getStatus().equals(JDA.Status.CONNECTED) && e.getJoinMessage() != null){
            MessageEmbed embed = embedFromPlayer(Color.GREEN, p, e.getJoinMessage());
            channel.sendMessageEmbeds(embed).queue();
        }
        timeJoined.put(p,System.currentTimeMillis());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        if(jda.getStatus().equals(JDA.Status.CONNECTED) && e.getQuitMessage() != null){
            Player p = e.getPlayer();
            String statsSession = formatSeconds((double) (System.currentTimeMillis() - timeJoined.get(p))/ 1000);
            String statsPlayTime = formatSeconds((double) p.getStatistic(Statistic.PLAY_ONE_MINUTE)/20);
            MessageEmbed embed = embedFromPlayer(Color.RED, p, e.getQuitMessage(),
                    new MessageEmbed.Field("Session Time:", statsSession, false),
                    new MessageEmbed.Field("Total Play Time:", statsPlayTime, false)
            );
            channel.sendMessageEmbeds(embed).queue();
        }
    }
}
