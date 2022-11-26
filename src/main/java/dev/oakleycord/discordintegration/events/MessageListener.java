package dev.oakleycord.discordintegration.events;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import dev.oakleycord.discordintegration.DiscordIntegration;
import dev.oakleycord.discordintegration.discord.ChatBot;
import dev.oakleycord.discordintegration.messages.MessageFormatter;
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

    private static final String AVATAR_URL = "https://crafatar.com/renders/head/%s?size=128&default=MHF_Steve&overlay";
    private final ChatBot chatBot;
    private final MessageFormatter formatter;
    private final DiscordIntegration main;

    public MessageListener(ChatBot chatBot, MessageFormatter formatter, DiscordIntegration main) {
        this.chatBot = chatBot;
        this.formatter = formatter;
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        String message = formatter.format(event);
        if (message.isBlank()) return;
        sendServerMessage(message);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        String message = formatter.format(event);
        if (message.isBlank()) return;
        sendServerMessage(message);
    }

    @EventHandler
    public void onSay(ServerCommandEvent event) {
        String message = formatter.format(event);
        if (message.isBlank()) return;
        sendServerMessage(message);
    }

    @EventHandler
    public void onCompleteAdvancement(PlayerAdvancementDoneEvent event) {
        String message = formatter.format(event);
        if (message.isBlank()) return;
        sendServerMessage(message);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        String message = formatter.format(event);
        if (message.isBlank()) return;
        sendServerMessage(message);
    }

    @EventHandler
    public void onBroadcast(BroadcastMessageEvent event) {
        String message = formatter.format(event);
        if (message.isBlank()) return;
        sendServerMessage(message);
    }

    private void sendServerMessage(String message) {
        main.getServer().getScheduler().runTaskAsynchronously(main, () -> {
            WebhookClient client = chatBot.getClient();
            WebhookMessageBuilder builder = new WebhookMessageBuilder();
            builder.setUsername(chatBot.getServerName());
            builder.setAvatarUrl(chatBot.getServerAvatar());
            builder.setContent(message);
            client.send(builder.build());
        });
    }

    @EventHandler
    public void onPlayerMessage(AsyncChatEvent event) {
        String message = formatter.format(event);
        if (message.isBlank()) return;
        main.getServer().getScheduler().runTaskAsynchronously(main, () -> {
            String uuid = event.getPlayer().getUniqueId().toString();

            WebhookClient client = chatBot.getClient();
            WebhookMessageBuilder builder = new WebhookMessageBuilder();
            builder.setUsername(event.getPlayer().getName());
            builder.setContent(message);
            builder.setAvatarUrl(String.format(AVATAR_URL, uuid));
            client.send(builder.build());
        });
    }


}
