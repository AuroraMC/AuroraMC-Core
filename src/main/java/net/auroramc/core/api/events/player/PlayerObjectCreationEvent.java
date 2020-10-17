package net.auroramc.core.api.events.player;

import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.event.HandlerList;

public class PlayerObjectCreationEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    private AuroraMCPlayer player;

    public PlayerObjectCreationEvent(AuroraMCPlayer player) {
        super(player);
        this.player = player;
    }

    public PlayerObjectCreationEvent(AuroraMCPlayer player, boolean isAsync) {
        super(player, isAsync);
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public AuroraMCPlayer getPlayer() {
        return player;
    }

    public void setPlayer(AuroraMCPlayer player) {
        this.player = player;
    }
}
