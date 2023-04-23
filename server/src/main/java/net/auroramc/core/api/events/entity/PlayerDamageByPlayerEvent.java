/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.events.entity;

import com.google.common.base.Function;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import java.util.Map;

public class PlayerDamageByPlayerEvent extends PlayerDamageEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final AuroraMCServerPlayer damaged;
    private boolean cancelled;

    public PlayerDamageByPlayerEvent(AuroraMCServerPlayer player, AuroraMCServerPlayer damaged, DamageCause cause, double damage) {
        super(player, cause, damage);
        this.damaged = damaged;
        cancelled = false;
    }

    public AuroraMCServerPlayer getDamaged() {
        return damaged;
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
