package dev.oakleycord.discordintegration.config;

import org.plusmc.pluslibcore.reflection.velocitybukkit.config.ConfigEntry;
import org.plusmc.pluslibcore.reflection.velocitybukkit.config.InjectableConfig;

public class MessagesConfig {
    @ConfigEntry
    private String death = "%message%";
    @ConfigEntry
    private String join = "%message%";
    @ConfigEntry
    private String leave = "%message%";
    @ConfigEntry
    private String chat = "%message%";

    @ConfigEntry
    private String reply = "%message%";
    @ConfigEntry
    private String advancement = "%message%";
    @ConfigEntry
    private String server = "%message%";
    @ConfigEntry
    private String broadcast = "%message%";
    @ConfigEntry
    private String discord = "%message%";

    public boolean highlightMessages() {
        return highlightMessages;
    }

    @ConfigEntry
    private boolean highlightMessages = true;
    public MessagesConfig(InjectableConfig config) {
        config.inject(this);
    }

    public String getChat() {
        return chat;
    }

    public String getDeath() {
        return death;
    }

    public String getJoin() {
        return join;
    }

    public String getLeave() {
        return leave;
    }

    public String getAdvancement() {
        return advancement;
    }

    public String getServer() {
        return server;
    }

    public String getBroadcast() {
        return broadcast;
    }

    public String getDiscord() {
        return discord;
    }

    public String getReply() {
        return reply;
    }
}
