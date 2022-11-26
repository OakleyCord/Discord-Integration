package dev.oakleycord.discordintegration.commands;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface SubCommand {
    boolean execute(CommandSender sender, String[] args);

    String getPermission();

    List<String> getTabCompletions(CommandSender sender, String[] args);

    String getDescription();

    String getName();
}
