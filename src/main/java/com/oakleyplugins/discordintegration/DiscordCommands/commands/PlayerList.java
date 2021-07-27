package com.oakleyplugins.discordintegration.DiscordCommands.commands;

import static com.oakleyplugins.discordintegration.Methods.*;

import com.oakleyplugins.discordintegration.DiscordCommands.DiscordCommand;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class PlayerList extends DiscordCommand {

    public PlayerList() {
        super();
    }

    @Override
    public @NotNull String getName() {
        return "playerlist";
    }


    @Override
    public @NotNull String getDescription() {
        return "A List of Players players playing on the server!";
    }

    @Override
    public @NotNull OptionData[] getOptionData() {
        return super.getOptionData();
    }

    @Override
    public CommandData getCommandData() {
        return super.getCommandData();
    }


    @Override
    public void execute(SlashCommandEvent e) {
        int i = Bukkit.getOnlinePlayers().size();
        String online = "There Are " + i + " Player(s) Online";
        StringBuilder list = new StringBuilder();
        for(Player p : Bukkit.getOnlinePlayers()){
            list.append(p.getName()).append(", ");
        }
        if(list.length() < 1) list.append("no one's here :(  ");
        MessageEmbed embed = embedFromCustom(Color.GREEN, online, list.substring(0, list.length() - 2));
        e.getInteraction().replyEmbeds(embed).queue();
    }

}
