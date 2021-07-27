package com.oakleyplugins.discordintegration;

import com.oakleyplugins.discordintegration.DiscordCommands.DiscordCommand;
import com.oakleyplugins.discordintegration.DiscordCommands.commands.CompletePlayTime;
import com.oakleyplugins.discordintegration.DiscordCommands.commands.PlayerList;
import com.oakleyplugins.discordintegration.DiscordCommands.commands.RandomStatistic;
import com.oakleyplugins.discordintegration.DiscordCommands.commands.PlayTime;
import com.oakleyplugins.discordintegration.Events.DiscordEvents;
import com.oakleyplugins.discordintegration.Events.MCEvents;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public final class DiscordIntegration extends JavaPlugin implements Listener {

    public static JDA jda;
    public static DiscordIntegration PLUGIN;
    public static TextChannel channel;
    public static boolean reconnecting = false;
    public static Logger logger;
    public static HashMap<Player,Long> timeJoined;

    @Override
    public void onEnable() {
        PLUGIN = this;
        timeJoined = new HashMap<>();
        logger = getLogger();
        getServer().getPluginManager().registerEvents(new MCEvents(), this);
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        loadCommands();
        reconnect();
    }

    public void loadCommands(){
        DiscordCommand.commands = new ArrayList<>();
        new PlayerList();
        new RandomStatistic();
        new CompletePlayTime();
        new PlayTime();
        logger.info("Loaded commands!");
    }

    public static void reconnect() {
        Bukkit.getScheduler().runTaskAsynchronously(PLUGIN, ()->{
            logger.info("Reconnecting...");
            if(!reconnecting) {
                try {
                    reconnecting = true;
                    startBot();
                    reconnecting = false;
                    logger.info("Reconnection Success!");
                } catch (Exception e) {
                    reconnecting = false;
                    logger.warning("Could Not Reconnect! Trying Again in 5 Minutes!");
                    logger.warning("Reconnection Error: " + e.getMessage());
                    e.printStackTrace();
                    Bukkit.getScheduler().runTaskLater(PLUGIN, DiscordIntegration::reconnect,6000);
                }
            }
        });
    }

    public static void startBot() throws Exception {
        String token = PLUGIN.getConfig().getString( "Token");
        String channel = PLUGIN.getConfig().getString("Channel");
        try{
            jda.shutdown();
        } catch (Exception e) {
            jda = null;
        }
        jda = JDABuilder.createDefault(token)
                .build();
        jda.awaitReady();
        if(channel == null) throw new Exception("Channel ID is Invalid!");
        DiscordIntegration.channel = jda.getTextChannelById(channel);
        if(DiscordIntegration.channel == null) throw new Exception("Channel ID is Invalid!");
        jda.setAutoReconnect(true);
        logger.info("Updating Commands...");
        jda.updateCommands().addCommands(DiscordCommand.getAllCommandData()).complete();
        logger.info("Updated!");
        logger.info("Registered Commands!");
        jda.addEventListener(new DiscordEvents());
    }

    @Override
    public void onDisable() {
        logger.warning("Disabling the bot may throw some errors but just ignore it");
        try{
            jda.shutdown();
        } catch (Exception e) {
            jda = null;
        }
    }

}
