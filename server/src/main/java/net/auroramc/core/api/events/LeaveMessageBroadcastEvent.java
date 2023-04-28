/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.events;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LeaveMessageBroadcastEvent extends Event implements Cancellable {

    public enum BroadcastReason {DISGUISE, VANISH, LEAVE, CUSTOM}

    private static final HandlerList handlers = new HandlerList();

    private boolean isCancelled;
    private final AuroraMCServerPlayer player;
    private BroadcastReason reason;
    private String message;

    public LeaveMessageBroadcastEvent(AuroraMCServerPlayer player, BroadcastReason reason, String message) {
        this.isCancelled = false;
        this.player = player;
        this.reason = reason;
        this.message = message;
    }

    public AuroraMCServerPlayer getPlayer() {
        return player;
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
