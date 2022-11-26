package dev.oakleycord.discordintegration.commands;

import dev.oakleycord.discordintegration.DiscordIntegration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscordCommand implements CommandExecutor, TabCompleter {
    private final List<SubCommand> subCommands;

    private final DiscordIntegration main;

    public DiscordCommand(DiscordIntegration main) {
        subCommands = new ArrayList<>();
        subCommands.add(new Reload(main));
        this.main = main;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 0) {
            commandSender.sendMessage("Usage: /discord <subcommand>");
            return true;
        }

        for (SubCommand subCommand : subCommands) {
            if (subCommand.getName().equalsIgnoreCase(args[0])) {
                if (commandSender.hasPermission(subCommand.getPermission())) {
                    return subCommand.execute(commandSender, Arrays.copyOfRange(args, 1, args.length));
                } else {
                    commandSender.sendMessage("You do not have permission to use this command");
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        return args.length == 1 ? subCommands.stream().filter(subCommand -> sender.hasPermission(subCommand.getPermission())).map(SubCommand::getName).toList() :
                subCommands.stream().filter(subCommand -> subCommand.getName().equalsIgnoreCase(args[0])).findFirst().map(subCommand ->
                        subCommand.getTabCompletions(sender, Arrays.copyOfRange(args, 1, args.length))
                ).orElse(null);
    }
}
