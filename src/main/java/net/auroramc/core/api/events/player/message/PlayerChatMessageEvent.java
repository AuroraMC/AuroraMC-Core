package net.auroramc.core.api.events.player.message;

import net.auroramc.core.api.players.AuroraMCPlayer;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.event.HandlerList;

public class PlayerChatMessageEvent extends PlayerMessageEvent {

    private static final HandlerList handlers = new HandlerList();

    private TextComponent formattedMessage;

    public PlayerChatMessageEvent(AuroraMCPlayer player, String rawMessage, TextComponent formattedMessage) {
        super(player, rawMessage);
        this.formattedMessage = formattedMessage;
    }

    public PlayerChatMessageEvent(AuroraMCPlayer player, String rawMessage, TextComponent formattedMessage, boolean isAsync) {
        super(player, rawMessage, isAsync);
        this.formattedMessage = formattedMessage;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public TextComponent getFormattedMessage() {
        return formattedMessage;
    }

    public void setFormattedMessage(TextComponent formattedMessage) {
        this.formattedMessage = formattedMessage;
    }
}
