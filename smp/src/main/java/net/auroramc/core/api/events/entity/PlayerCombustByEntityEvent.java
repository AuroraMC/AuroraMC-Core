/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.core.api.events.entity;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerCombustByEntityEvent extends PlayerCombustEvent implements Cancellable {

    private static final HandlerList handlerList = new HandlerList();

    private final Entity damager;

    public PlayerCombustByEntityEvent(AuroraMCServerPlayer player, int duration, Entity damager) {
        super(player, duration);
        this.damager = damager;
    }

    public Entity getDamager() {
        return damager;
    }

    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

}