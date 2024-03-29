/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.api.events.entity;

import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class EntityCombustByPlayerEvent extends PlayerCombustEvent implements Cancellable {

    private static final HandlerList handlerList = new HandlerList();

    private final Entity damaged;

    public EntityCombustByPlayerEvent(AuroraMCServerPlayer player, int duration, Entity damaged) {
        super(player, duration);
        this.damaged = damaged;
    }

    public Entity getDamaged() {
        return damaged;
    }

    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

}