package net.auroramc.core.api.events.cosmetics;

import net.auroramc.core.api.cosmetics.Cosmetic;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CosmeticEnableEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled = false;
    private final AuroraMCPlayer player;
    private Cosmetic cosmetic;

    public CosmeticEnableEvent(AuroraMCPlayer player, Cosmetic cosmetic) {
        this.player = player;
        this.cosmetic = cosmetic;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public Cosmetic getCosmetic() {
        return cosmetic;
    }

    public void setCosmetic(Cosmetic cosmetic) {
        this.cosmetic = cosmetic;
    }

    public AuroraMCPlayer getPlayer() {
        return player;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}