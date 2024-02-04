/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.core.api.events.player;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerJoinMessageBroadcastEvent extends PlayerEvent implements Cancellable {

    public enum BroadcastReason {DISGUISE, UNVANISH, JOIN, CUSTOM}

    private static final HandlerList handlers = new HandlerList();

    private boolean isCancelled;
    private BroadcastReason reason;
    private String message;

    public PlayerJoinMessageBroadcastEvent(AuroraMCServerPlayer player, BroadcastReason reason, String message) {
        super(player);
        this.isCancelled = false;
        this.reason = reason;
        this.message = message;
    }

    public BroadcastReason getReason() {
        return reason;
    }

    public void setReason(BroadcastReason reason) {
        this.reason = reason;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
