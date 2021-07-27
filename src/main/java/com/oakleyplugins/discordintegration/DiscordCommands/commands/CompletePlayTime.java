package com.oakleyplugins.discordintegration.DiscordCommands.commands;

import com.oakleyplugins.discordintegration.DiscordCommands.DiscordCommand;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.oakleyplugins.discordintegration.Methods.embedFromCustom;
import static com.oakleyplugins.discordintegration.Methods.formatSeconds;

public class CompletePlayTime extends DiscordCommand {

    public CompletePlayTime() {
        super();
    }

    @Override
    public @NotNull String getName() {
        return "completeplaytime";
    }

    @Override
    public @NotNull String getDescription() {
        return "Gives you the playtime of all the members on the server";
    }

    @Override
    public @NotNull OptionData[] getOptionData() {
        return super.getOptionData();
    }

    @Override
    public CommandData getCommandData() {
        return super.getCommandData();
    }

    public List<MessageEmbed.Field> getPlayTime(){
        List<MessageEmbed.Field> fields = new LinkedList<>();
        double inTotal = 0;
        for(OfflinePlayer p : Bukkit.getOfflinePlayers()){
            if(p.getName() != null)
            if(!p.getName().equalsIgnoreCase("dream")){
                double time = (double) p.getStatistic(Statistic.PLAY_ONE_MINUTE)/20;
                inTotal+=time;
                String statsPlayTime = formatSeconds(time);
                fields.add(new MessageEmbed.Field(p.getName() + ":","Total Play Time:\n" + statsPlayTime, true));
            }
        }
        String statsPlayTime = formatSeconds(inTotal);
        fields.add(new MessageEmbed.Field("Total of All Members:","Total Play Time:\n" + statsPlayTime, true));
        return fields;
    }

    public <T> List<List<T>> chopped(List<T> list, final int L) {
        List<List<T>> parts = new ArrayList<>();
        final int N = list.size();
        for (int i = 0; i < N; i += L) {
            parts.add(new ArrayList<T>(
                    list.subList(i, Math.min(N, i + L)))
            );
        }
        return parts;
    }


    @Override
    public void execute(SlashCommandEvent e) {
        List<List<MessageEmbed.Field>> chopped = chopped(getPlayTime(),24);
        List<MessageEmbed> embeds = new ArrayList<>();
        for (List<MessageEmbed.Field> field : chopped){
            MessageEmbed embed = embedFromCustom(Color.GREEN, null, null,"Playtime of All Members", field.toArray(new MessageEmbed.Field[0]));
            embeds.add(embed);
        }
        e.replyEmbeds(embeds).queue();
    }
}
