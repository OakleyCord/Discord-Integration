package com.oakleyplugins.discordintegration.DiscordCommands.commands;

import com.oakleyplugins.discordintegration.DiscordCommands.DiscordCommand;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static com.oakleyplugins.discordintegration.Methods.embedFromCustom;
import static com.oakleyplugins.discordintegration.Methods.formatSeconds;

public class RandomStatistic extends DiscordCommand {
    Random rand;
    ArrayList<Material> blocks;

    public RandomStatistic() {
        super();
        blocks = new ArrayList<>();
        for(Material material : Material.values()) {
            if(material.isBlock()) blocks.add(material);
        }
        rand = new Random();
    }

    @Override
    public @NotNull String getName() {
        return "randomstatistic";
    }

    @Override
    public @NotNull String getDescription() {
        return "Get a random statistic";
    }

    @Override
    public @NotNull OptionData[] getOptionData() {
        OptionData data = new OptionData(OptionType.STRING, "playername", "Player's name", false);
        return new OptionData[]{data};
    }

    @Override
    public CommandData getCommandData() {
        return super.getCommandData();
    }

    public Material getRandomBlock(){
        return blocks.get(rand.nextInt(blocks.size()));
    }

    public Material getRandomItem(){
        int i = Material.values().length;
        return Material.values()[rand.nextInt(i)];
    }

    public EntityType getRandomEntity(){
        int i = EntityType.values().length;
        return EntityType.values()[rand.nextInt(i)];
    }
    public  String randStat(OfflinePlayer p){
        int randInt = rand.nextInt(19);
        Material item = getRandomItem();
        EntityType entity = getRandomEntity();
        Material block = getRandomBlock();
        switch(randInt){
                case 0:
                return p.getName() + " has survived for " + formatSeconds((double) p.getStatistic(Statistic.TIME_SINCE_DEATH)/20);
            case 1:
                return p.getName() + " has a total playtime of " + formatSeconds((double) p.getStatistic(Statistic.PLAY_ONE_MINUTE)/20);
            case 2:
                return p.getName() + " has rung " + p.getStatistic(Statistic.BELL_RING) + " bells";
            case 3:
                return p.getName() + " has mined " + p.getStatistic(Statistic.MINE_BLOCK, block) + " " + block.name();
            case 4:
                return p.getName() + " smells";
            case 5:
                return p.getName() + " has killed " + p.getStatistic(Statistic.KILL_ENTITY, entity) + " " + entity.getName();
            case 6:
                return p.getName() + " needs to take a shower";
            case 7:
                return p.getName() + " has used/placed " + item + " " + p.getStatistic(Statistic.USE_ITEM, item) + " times";
            case 8:
                return p.getName() + " has picked up " + item + " " + p.getStatistic(Statistic.PICKUP, item) + " times";
            case 9:
                return p.getName() + " has dropped " + item + " " + p.getStatistic(Statistic.DROP, item) + " times";
            case 10:
                return p.getName() + " has died " + p.getStatistic(Statistic.DEATHS) + " times";
            case 11:
                return p.getName() + " has jumped " + p.getStatistic(Statistic.JUMP) + " times";
            case 12:
                return p.getName() + " has dropped " + p.getStatistic(Statistic.DROP_COUNT) + " items";
            case 13:
                return p.getName() + " has walked " +  (p.getStatistic(Statistic.WALK_ONE_CM)/100)  + " meters";
            case 14:
                return p.getName() + " has sprinted " + (p.getStatistic(Statistic.SPRINT_ONE_CM)/100) + " meters";
            case 15:
                return p.getName() + " has flown " +  (p.getStatistic(Statistic.AVIATE_ONE_CM)/100) + " meters on a elytra";
            case 16:
                return p.getName() + " has bred " + p.getStatistic(Statistic.ANIMALS_BRED) +  " animals";
            case 17:
                return "no";
            case 18:
                return "boo";
         }
        return "Error finding statistic";
    }

    @Override
    public void execute(SlashCommandEvent e) {
        OptionMapping optionMap = e.getOption("playername");
        String stat = "Invalid Player Name";
        Color color = Color.RED;
        if(optionMap != null){
            String name = optionMap.getAsString();
            for(OfflinePlayer p : Bukkit.getOfflinePlayers())
                if(p.getName() != null)
                    if(p.getName().equalsIgnoreCase(name)) { stat = randStat(p); color = Color.GREEN; }
        } else {
            OfflinePlayer[] list = Bukkit.getOfflinePlayers();
            if(list.length > 0){
                int randInt = rand.nextInt(list.length);
                stat = randStat(list[randInt]);
                color = Color.GREEN;
            }
        }
        MessageEmbed embed = embedFromCustom(color, "Random Statistic", stat);
        e.replyEmbeds(embed).queue();
    }
}
