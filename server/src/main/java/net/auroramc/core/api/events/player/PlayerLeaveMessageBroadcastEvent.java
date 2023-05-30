/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.core.api.events.player;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerLeaveMessageBroadcastEvent extends PlayerEvent implements Cancellable {

    public enum BroadcastReason {DISGUISE, VANISH, LEAVE, CUSTOM}

    private static final HandlerList handlers = new HandlerList();

    private boolean isCancelled;
    private BroadcastReason reason;
    private String message;

    public PlayerLeaveMessageBroadcastEvent(AuroraMCServerPlayer player, BroadcastReason reason, String message) {
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
