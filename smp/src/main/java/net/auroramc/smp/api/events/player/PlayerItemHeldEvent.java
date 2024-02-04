/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.api.events.player;

import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerItemHeldEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final int previous;
    private final int current;
    private boolean cancelled;

    public PlayerItemHeldEvent(AuroraMCServerPlayer player, int previous, int current) {
        super(player);
        this.previous = previous;
        this.current = current;
    }

    public int getCurrentSlot() {
        return current;
    }

    public int getPreviousSlot() {
        return previous;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

}
