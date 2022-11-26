package dev.oakleycord.discordintegration.messages;

import dev.oakleycord.discordintegration.DiscordIntegration;
import dev.oakleycord.discordintegration.config.MessagesConfig;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.BroadcastMessageEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.jetbrains.annotations.Nullable;


public class MessageFormatter {

    private final MessagesConfig messagesConfig;
    private final DiscordIntegration main;

    public MessageFormatter(MessagesConfig config, DiscordIntegration main) {
        this.messagesConfig = config;
        this.main = main;
    }

    public MessagesConfig getMessagesConfig() {
        return messagesConfig;
    }

    //most readable code ever /s
    public String format(@Nullable Object event) {
        //time for if hell
        String message = "";
        if (event instanceof PlayerDeathEvent deathEvent) {
            message = messagesConfig.getDeath();
            if (deathEvent.deathMessage() != null) {
                String deathMessage = PlainTextComponentSerializer.plainText().serialize(deathEvent.deathMessage());
                message = message.replace("%message%", deathMessage);
            }
        }

        if (event instanceof PlayerAdvancementDoneEvent advancementDoneEvent) {
            //this is so it does not trigger advancements not announced
            if (advancementDoneEvent.message() == null) return "";
            message = messagesConfig.getAdvancement();

            String advancementMessage = PlainTextComponentSerializer.plainText().serialize(advancementDoneEvent.message());
            message = message.replace("%message%", advancementMessage);


            String object = PlainTextComponentSerializer.plainText().serialize(advancementDoneEvent.getAdvancement().displayName());
            message = message.replace("%object%", object);
        }

        if (event instanceof BroadcastMessageEvent broadcastMessageEvent) {
            message = messagesConfig.getBroadcast();
            String broadcastMessage = PlainTextComponentSerializer.plainText().serialize(broadcastMessageEvent.message());
            message = message.replace("%message%", broadcastMessage);
        }

        if (event instanceof AsyncChatEvent chatEvent) {
            message = messagesConfig.getChat();
            String chatMessage = PlainTextComponentSerializer.plainText().serialize(chatEvent.message());
            message = message.replace("%message%", chatMessage);
        }

        if (event instanceof ServerCommandEvent serverCommandEvent) {
            message = messagesConfig.getServer();
            String command = serverCommandEvent.getCommand();
            if (command.startsWith("say ")) {
                String sayMessage = command.substring(4);
                message = message.replace("%message%", sayMessage);
            }
        }

        if (event instanceof PlayerJoinEvent playerJoinEvent) {
            message = messagesConfig.getJoin();
            if (playerJoinEvent.joinMessage() != null) {
                String joinMessage = PlainTextComponentSerializer.plainText().serialize(playerJoinEvent.joinMessage());
                message = message.replace("%message%", joinMessage);
            }
        }

        if (event instanceof PlayerQuitEvent playerQuitEvent) {
            message = messagesConfig.getLeave();
            if (playerQuitEvent.quitMessage() != null) {
                String quitMessage = PlainTextComponentSerializer.plainText().serialize(playerQuitEvent.quitMessage());
                message = message.replace("%message%", quitMessage);
            }
        }

        if (event instanceof MessageReceivedEvent messageReceivedEvent) {
            message = messagesConfig.getDiscord();
            Message referencedMessage = messageReceivedEvent.getMessage().getReferencedMessage();
            if (referencedMessage != null) {
                message = messagesConfig.getReply();
                message = message.replace("%reply%", referencedMessage.getAuthor().getName());
            }

            message = message.replace("%message%", messageReceivedEvent.getMessage().getContentRaw());
            message = message.replace("%author%", messageReceivedEvent.getAuthor().getName());
            message = ChatColor.translateAlternateColorCodes('&', message);
        }

        if (event instanceof PlayerEvent playerEvent) {
            message = message.replace("%player%", playerEvent.getPlayer().getName());
            message = message.replace("%world%", playerEvent.getPlayer().getWorld().getName());
        }

        String serverName = main.getServer().getName();
        message = message.replace("%server%", serverName);

        String maxPlayers = String.valueOf(main.getServer().getMaxPlayers());
        message = message.replace("%maxplayers%", maxPlayers);

        String onlinePlayers = String.valueOf(main.getServer().getOnlinePlayers().size());
        message = message.replace("%onlineplayers%", onlinePlayers);

        String motd = PlainTextComponentSerializer.plainText().serialize(main.getServer().motd());
        message = message.replace("%motd%", motd);

        return message;
    }

}
