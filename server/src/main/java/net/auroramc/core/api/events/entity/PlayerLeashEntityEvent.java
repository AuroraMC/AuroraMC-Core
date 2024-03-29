/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.core.api.events.entity;

import net.auroramc.core.api.events.player.PlayerEvent;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerLeashEntityEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled;
    private final Entity entity;
    private final Entity leash;


    public PlayerLeashEntityEvent(AuroraMCServerPlayer player, Entity entity, Entity leash) {
        super(player);
        this.cancelled = false;
        this.entity = entity;
        this.leash = leash;
    }

    public Entity getEntity() {
        return entity;
    }

    public Entity getLeash() {
        return leash;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
