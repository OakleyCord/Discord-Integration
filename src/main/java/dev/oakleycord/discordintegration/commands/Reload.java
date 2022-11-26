package dev.oakleycord.discordintegration.commands;

import dev.oakleycord.discordintegration.DiscordIntegration;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class Reload implements SubCommand {

    private final DiscordIntegration main;

    public Reload(DiscordIntegration main) {
        this.main = main;
    }
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        main.onDisable();
        main.onEnable();
        sender.sendMessage(ChatColor.GREEN + "Reloaded Discord Integration!");
        return true;
    }

    @Override
    public String getPermission() {
        return "discordintegration.reload";
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        return List.of("");
    }

    @Override
    public String getDescription() {
        return "Reloads the plugin";
    }

    @Override
    public String getName() {
        return "reload";
    }
}
