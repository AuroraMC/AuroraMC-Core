/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.core.api.events.player;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class AsyncPlayerChatEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private String message;
    private boolean cancelled;

    public AsyncPlayerChatEvent(boolean async, AuroraMCServerPlayer player, String message) {
        super(player, async);
        this.message = message;
        cancelled = false;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
        cancelled = b;
    }
}
