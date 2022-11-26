package dev.oakleycord.discordintegration;

import dev.oakleycord.discordintegration.commands.DiscordCommand;
import dev.oakleycord.discordintegration.config.MessagesConfig;
import dev.oakleycord.discordintegration.discord.ChatBot;
import dev.oakleycord.discordintegration.events.MessageListener;
import dev.oakleycord.discordintegration.messages.MessageFormatter;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.plusmc.pluslibcore.reflection.velocitybukkit.config.InjectableConfig;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class DiscordIntegration extends JavaPlugin {


    private InjectableConfig config;

    private ChatBot chatBot;
    private MessagesConfig messagesConfig;
    private MessageFormatter messageFormatter;

    @Override
    public void onEnable() {
        DiscordCommand command = new DiscordCommand(this);

        PluginCommand discordCommand = getCommand("discord");
        if (discordCommand != null) {
            discordCommand.setExecutor(command);
            discordCommand.setTabCompleter(command);
        } else {
            getLogger().log(Level.SEVERE, "Could not register discord command");
        }

        if (loadBot())
            getServer().getPluginManager().registerEvents(new MessageListener(chatBot, messageFormatter, this), this);
    }

    public MessageFormatter getMessageFormatter() {
        return messageFormatter;
    }

    public boolean loadBot() {
        saveDefaultConfig();
        try {
            this.config = InjectableConfig.create(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            getLogger().info("Failed to load config");
            return false;
        }
        this.messagesConfig = new MessagesConfig(config.section("messages"));
        this.messageFormatter = new MessageFormatter(messagesConfig, this);
        chatBot = new ChatBot(config, this);
        return chatBot.loaded();
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        chatBot.close();
    }

}
