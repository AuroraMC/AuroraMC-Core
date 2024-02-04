/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.api.events.player;

import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerToggleFlightEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final boolean flying;
    private boolean cancelled;

    public PlayerToggleFlightEvent(AuroraMCServerPlayer player, boolean flying) {
        super(player);
        this.flying = flying;
        cancelled = false;
    }


    public boolean isFlying() {
        return flying;
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
