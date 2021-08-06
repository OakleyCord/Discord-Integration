package com.oakleyplugins.discordintegration;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public final class DiscordIntegration extends JavaPlugin implements Listener {

    public static JDA jda;
    public static DiscordIntegration PLUGIN;
    public static TextChannel channel;
    public static boolean reconnecting = false;
    private static BukkitTask versionCheck = null;
    public static Logger logger;
    public static HashMap<Player,Long> timeJoined;

    @Override
    public void onEnable() {
        PLUGIN = this;
        timeJoined = new HashMap<>();
        logger = getLogger();
        versionCheck = Bukkit.getScheduler().runTaskTimerAsynchronously(PLUGIN,this::checkVersion,0,144000);
        getServer().getPluginManager().registerEvents(new MCEvents(), this);
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        loadCommands();
        reconnect();
    }

    public @NotNull String checkVersionJson() throws Exception {
        String url = "https://api.github.com/repos/HappyCord/Discord-Integration/releases/latest";
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("application", "vnd.github.v3+json");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        con.setDoOutput(true);
        String line;
        StringBuilder output = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        while ((line = in.readLine()) != null)
            output.append(line);

        in.close();
        return output.toString();
    }

    public void checkVersion() {
        logger.info("Checking Version...");
        try{
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(checkVersionJson());
            JsonObject obj = element.getAsJsonObject();
            if(!obj.isJsonNull()){
                if(obj.has("tag_name")){
                    String version = obj.get("tag_name").getAsString();
                    if(!version.equalsIgnoreCase(PLUGIN.getDescription().getVersion())){
                        logger.info("========================");
                        logger.info("");
                        logger.info("New Version: " + version);
                        if(obj.has("body")){
                            String desc = obj.get("body").getAsString();
                            logger.info("");
                            logger.info("Description:");
                            logger.info(desc);
                            logger.info("");
                        }
                        logger.info("========================");
                    } else {
                        logger.info("No Updates Found!");
                    }
                }
            }
        } catch (Exception e){
            logger.warning("An Error Occurred When Checking Send The Error Bellow To Oakley");
            e.printStackTrace();
        }
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
        logger.info("Loading Commands...");
        jda.updateCommands().addCommands(DiscordCommand.getAllCommandData()).complete();
        logger.info("Loaded!");
        jda.addEventListener(new DiscordEvents());
    }

    @Override
    public void onDisable() {
        logger.warning("Disabling the bot may throw some errors but just ignore it");
        versionCheck.cancel();
        try{
            jda.shutdown();
        } catch (Exception e) {
            jda = null;
        }
    }

}
