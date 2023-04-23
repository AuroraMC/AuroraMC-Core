/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.events.player;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;

public class PlayerInteractAtEntityEvent extends PlayerInteractEntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Vector position;
    private boolean cancelled;

    public PlayerInteractAtEntityEvent(AuroraMCServerPlayer player, Entity clickedEntity, Vector position) {
        super(player, clickedEntity);
        cancelled = false;
        this.position = position;
    }

    public Vector getPosition() {
        return position;
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
