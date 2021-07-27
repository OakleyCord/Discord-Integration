package com.oakleyplugins.discordintegration.Events;

import com.oakleyplugins.discordintegration.DiscordCommands.DiscordCommand;
import com.oakleyplugins.discordintegration.DiscordIntegration;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

import static com.oakleyplugins.discordintegration.Methods.*;
import static com.oakleyplugins.discordintegration.DiscordIntegration.jda;
import static com.oakleyplugins.discordintegration.DiscordIntegration.logger;

public class DiscordEvents extends ListenerAdapter {

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent e) {
        DiscordCommand command = DiscordCommand.findCommand(e.getName());
        logger.info(e.getUser().getName() + " ran command " + e.getName());
        if(command != null){
            logger.info("executing command " + e.getName() + "...");
            command.execute(e);
        } else {
            MessageEmbed embed = embedFromCustom(Color.RED, "Error", "Command not found :(");
            e.replyEmbeds(embed).queue();
        }
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent e) {
        if (jda.getStatus().equals(JDA.Status.CONNECTED)) {
            if (e.getChannel().getId().equalsIgnoreCase(DiscordIntegration.channel.getId())) {
                if (e.getMember() != null && !e.getAuthor().isBot()) {
                    String msg = ChatColor.GOLD + "[DISCORD] " + ChatColor.GREEN + e.getMember().getEffectiveName() + ": " + ChatColor.RESET + e.getMessage().getContentRaw();
                    Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(msg));
                    logger.info(msg);
                }
            }
        }
    }



}
