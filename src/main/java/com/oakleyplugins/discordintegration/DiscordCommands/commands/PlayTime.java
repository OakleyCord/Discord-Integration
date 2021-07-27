package com.oakleyplugins.discordintegration.DiscordCommands.commands;

import com.oakleyplugins.discordintegration.DiscordCommands.DiscordCommand;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

import static com.oakleyplugins.discordintegration.Methods.embedFromCustom;
import static com.oakleyplugins.discordintegration.Methods.formatSeconds;

public class PlayTime extends DiscordCommand {
    public PlayTime() {
        super();
    }

    @Override
    public @NotNull String getName() {
        return "playtime";
    }

    @Override
    public @NotNull String getDescription() {
        return "Get a player's total play time";
    }

    @Override
    public @NotNull OptionData[] getOptionData() {
        OptionData data = new OptionData(OptionType.STRING, "playername", "Player's name", true);
        return new OptionData[]{data};
    }

    @Override
    public CommandData getCommandData() {
        return super.getCommandData();
    }

    public MessageEmbed getEmbed(OfflinePlayer p){
        String statsPlayTime = formatSeconds((double) p.getStatistic(Statistic.PLAY_ONE_MINUTE)/20);
        String uuid = p.getUniqueId().toString();

        return embedFromCustom(Color.GREEN, p.getName() + "'s Total Play Time:","https://crafatar.com/renders/head/" + uuid + "?size=128&default=MHF_Steve&overlay", null, statsPlayTime);
    }


    @Override
    public void execute(SlashCommandEvent e) {
        OptionMapping optionMap = e.getOption("playername");
        MessageEmbed embed = embedFromCustom(Color.RED, "Total Play Time", "Invalid Player Name");
        if(optionMap != null){
            String name = optionMap.getAsString();
            for(OfflinePlayer p : Bukkit.getOfflinePlayers())
                if(p.getName() != null)
                    if(p.getName().equalsIgnoreCase(name))
                        embed = getEmbed(p);
        }
        e.replyEmbeds(embed).queue();
    }
}
