/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.api.events.entity;

import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerCombustByPlayerEvent extends PlayerCombustEvent implements Cancellable {

    private static final HandlerList handlerList = new HandlerList();

    private final AuroraMCServerPlayer damager;

    public PlayerCombustByPlayerEvent(AuroraMCServerPlayer player, int duration, AuroraMCServerPlayer damager) {
        super(player, duration);
        this.damager = damager;
    }

    public AuroraMCServerPlayer getDamager() {
        return damager;
    }

    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

}