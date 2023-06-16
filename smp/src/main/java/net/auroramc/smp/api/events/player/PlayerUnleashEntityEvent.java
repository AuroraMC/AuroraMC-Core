/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.api.events.player;

import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerUnleashEntityEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Entity what;
    private boolean cancelled;

    public PlayerUnleashEntityEvent(AuroraMCServerPlayer player, Entity what) {
        super(player);
        this.what = what;
        cancelled = false;
    }

    public Entity getEntity() {
        return what;
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
