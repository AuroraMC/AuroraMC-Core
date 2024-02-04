/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.api.events.entity;

import net.auroramc.smp.api.events.player.PlayerEvent;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerCombustEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlerList = new HandlerList();
    private boolean cancelled;
    private int duration;

    public PlayerCombustEvent(AuroraMCServerPlayer player, int duration) {
        super(player);
        cancelled = false;
        this.duration = duration;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
