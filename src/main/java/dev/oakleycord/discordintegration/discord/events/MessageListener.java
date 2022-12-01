package dev.oakleycord.discordintegration.discord.events;

import dev.oakleycord.discordintegration.DiscordIntegration;
import dev.oakleycord.discordintegration.discord.ChatBot;
import dev.oakleycord.discordintegration.messages.PaperMessageFormatter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public class MessageListener extends ListenerAdapter {

    private final ChatBot chatBot;
    private final PaperMessageFormatter formatter;
    private final DiscordIntegration main;

    public MessageListener(ChatBot chatBot, DiscordIntegration main) {
        this.chatBot = chatBot;
        this.main = main;
        this.formatter = main.getMessageFormatter();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(chatBot.getChatChannel() == null)
            return;

        if (event.getChannel().getIdLong() != chatBot.getChatChannel().getIdLong())
            return;

        if (event.isWebhookMessage())
            return;

        String replying = "";
        if (event.getMessage().getReferencedMessage() != null && formatter.getMessagesConfig().highlightMessages()) {
            replying = event.getMessage().getReferencedMessage().getAuthor().getName();
        }

        String message = formatter.format(event);
        if (message.isBlank()) return;
        //to not trigger the broadcast event
        String finalReplying = replying;
        main.getServer().getOnlinePlayers().forEach(player -> {
            if(player.getName().equals(finalReplying))
                player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + message);
            else player.sendMessage(message);
        });
        main.getLogger().info(message);
    }
}
