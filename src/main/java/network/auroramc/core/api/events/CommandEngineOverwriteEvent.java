package network.auroramc.core.api.events;

import network.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CommandEngineOverwriteEvent extends Event implements Cancellable {

    private static final HandlerList handlerList = new HandlerList();
    private boolean cancelled;
    private final String message;
    private final AuroraMCPlayer player;

    public CommandEngineOverwriteEvent(String message, AuroraMCPlayer player) {
        cancelled = false;
        this.message = message;
        this.player = player;
    }

    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

    public String getMessage() {
        return message;
    }

    public AuroraMCPlayer getPlayer() {
        return player;
    }
}
