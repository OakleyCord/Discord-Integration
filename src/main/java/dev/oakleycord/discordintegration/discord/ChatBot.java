package dev.oakleycord.discordintegration.discord;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.mongodb.lang.Nullable;
import dev.oakleycord.discordintegration.DiscordIntegration;
import dev.oakleycord.discordintegration.discord.events.MessageListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.OfflinePlayer;
import org.plusmc.pluslibcore.reflection.velocitybukkit.config.ConfigEntry;
import org.plusmc.pluslibcore.reflection.velocitybukkit.config.InjectableConfig;


public class ChatBot {
    private static final String AVATAR_API = "https://crafatar.com/renders/head/%s?size=128&default=MHF_Steve&overlay";
    private final DiscordIntegration main;
    private final InjectableConfig config;
    private JDA jda;
    @ConfigEntry
    private String token;
    @ConfigEntry
    private String channel;
    @ConfigEntry
    private String webhook = "";
    @ConfigEntry
    private String serverAvatar = "";
    @ConfigEntry
    private String serverName = "Server";

    @Nullable
    private TextChannel queuedChannel;

    @Nullable
    private WebhookClient webhookClient;

    public ChatBot(InjectableConfig config, DiscordIntegration main) {
        this.main = main;
        this.config = config;
        config.inject(this);

        //TODO: Implement memory optimization
        try {
            this.jda = JDABuilder.createDefault(token)
                    .setBulkDeleteSplittingEnabled(false)
                    .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                    .addEventListeners(new MessageListener(this, main))
                    .build();
        } catch (Exception e) {
            main.getLogger().info("Failed to load bot");
            this.jda = null;
        }

    }

    public boolean loaded() {
        return jda != null;
    }

    @Nullable
    public WebhookClient getClient() {
        return webhookClient == null ? webhookClient = createClient(this.getChatChannel()) : webhookClient;
    }

    private WebhookClient createClient(TextChannel channel) {
        String[] split = webhook.split("::");
        if (webhook.isBlank() || split.length != 2 || !split[0].equalsIgnoreCase(channel.getId())) {
            Webhook webhookChannel = channel.createWebhook("MinecraftIntegration").complete();
            webhook = channel.getId() + "::" + webhookChannel.getUrl();
            this.save();
            main.getLogger().info("Created webhook for channel " + channel.getName());
        }
        split = webhook.split("::");
        if (split.length != 2)
            throw new IllegalStateException("Webhook is not in the correct format: " + webhook);

        return WebhookClient.withUrl(split[1]);
    }

    private void save() {
        try {
            config.fromObject(this);
            config.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getServerAvatar() {
        return serverAvatar;
    }

    public String getServerName() {
        return serverName;
    }

    public void close() {
        if (webhookClient != null) {
            webhookClient.close();
        }
        //this errors out sometimes
        jda.shutdownNow();
    }

    public void sendServerMessage(String message) {
        sendWebhook(getServerName(), getServerAvatar(), message);
    }

    public void sendPlayerMessage(OfflinePlayer player, String message) {
        String uuid = player.getUniqueId().toString();
        String name = player.getName() == null ? uuid : player.getName();
        sendWebhook(name, String.format(AVATAR_API, uuid), message);

    }


    public void sendWebhook(String username, String avatar, String message) {
        if (message.isBlank()) return;
        //make sure to run on a different thread
        main.getServer().getScheduler().runTaskAsynchronously(main, () -> {
            WebhookClient client = getClient();
            if (client == null)
                return;

            WebhookMessageBuilder builder = new WebhookMessageBuilder()
                    .setUsername(username)
                    .setAvatarUrl(avatar)
                    .setContent(message);
            client.send(builder.build());
        });
    }

    @Nullable
    public TextChannel getChatChannel() {
        if (queuedChannel == null) {
            queuedChannel = jda.getTextChannelById(channel);
        }
        return queuedChannel;
    }

    public JDA getJDA() {
        return jda;
    }
}
