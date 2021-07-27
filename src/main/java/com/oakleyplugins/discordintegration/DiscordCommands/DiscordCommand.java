package com.oakleyplugins.discordintegration.DiscordCommands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class DiscordCommand {

    public static ArrayList<DiscordCommand> commands = new ArrayList<>();

    public DiscordCommand(){
        commands.add(this);
    }

    @NotNull
    public String getName(){
        return "null";
    }

    @NotNull
    public String getDescription(){
        return "null";
    }

    @NotNull
    public OptionData[] getOptionData(){
        return new OptionData[0];
    }


    public CommandData getCommandData(){
        CommandData data = new CommandData(this.getName(), this.getDescription());
        data.setDefaultEnabled(true);
        data.addOptions(this.getOptionData());
        return data;
    }

    public void execute(SlashCommandEvent event) {
        event.reply("Default Reply").queue();
    }

    public static List<CommandData> getAllCommandData(){
        ArrayList<CommandData> data = new ArrayList<>();
        commands.forEach(cmd -> data.add(cmd.getCommandData()));
        return data;
    }
     public static DiscordCommand findCommand(String name){
        for(DiscordCommand command : commands){
            if(command.getName().equalsIgnoreCase(name))
                return command;
        }
        return null;
    }
}
