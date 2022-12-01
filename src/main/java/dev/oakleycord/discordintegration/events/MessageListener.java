package dev.oakleycord.discordintegration.events;

import dev.oakleycord.discordintegration.DiscordIntegration;
import dev.oakleycord.discordintegration.discord.ChatBot;
import dev.oakleycord.discordintegration.messages.PaperMessageFormatter;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.BroadcastMessageEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class MessageListener implements Listener {
    private final ChatBot chatBot;
    private final PaperMessageFormatter formatter;
    private final DiscordIntegration main;

    public MessageListener(ChatBot chatBot, PaperMessageFormatter formatter, DiscordIntegration main) {
        this.chatBot = chatBot;
        this.formatter = formatter;
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        String message = formatter.format(event);
        chatBot.sendServerMessage(message);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        String message = formatter.format(event);
        chatBot.sendServerMessage(message);
    }

    @EventHandler
    public void onSay(ServerCommandEvent event) {
        String message = formatter.format(event);
        chatBot.sendServerMessage(message);
    }

    @EventHandler
    public void onCompleteAdvancement(PlayerAdvancementDoneEvent event) {
        String message = formatter.format(event);
        chatBot.sendServerMessage(message);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        String message = formatter.format(event);
        chatBot.sendServerMessage(message);
    }

    @EventHandler
    public void onBroadcast(BroadcastMessageEvent event) {
        String message = formatter.format(event);
        chatBot.sendServerMessage(message);
    }


    @EventHandler
    public void onPlayerMessage(AsyncChatEvent event) {
        String message = formatter.format(event);
        chatBot.sendPlayerMessage(event.getPlayer(), message);
    }


}
