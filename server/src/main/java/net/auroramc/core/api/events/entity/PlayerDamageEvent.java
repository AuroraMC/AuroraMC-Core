/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.core.api.events.entity;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import net.auroramc.core.api.events.player.PlayerEvent;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.apache.commons.lang.Validate;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import java.util.EnumMap;
import java.util.Map;

public class PlayerDamageEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled;
    private final DamageCause cause;
    private double damage;

    public PlayerDamageEvent(AuroraMCServerPlayer player, DamageCause cause, double damage) {
        super(player);
        this.cause = cause;
        cancelled = false;
        this.damage = damage;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public DamageCause getCause() {
        return this.cause;
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

    public static enum DamageCause {
        CONTACT,
        ENTITY_ATTACK,
        PROJECTILE,
        SUFFOCATION,
        FALL,
        FIRE,
        FIRE_TICK,
        MELTING,
        LAVA,
        DROWNING,
        BLOCK_EXPLOSION,
        ENTITY_EXPLOSION,
        VOID,
        LIGHTNING,
        SUICIDE,
        STARVATION,
        POISON,
        MAGIC,
        WITHER,
        FALLING_BLOCK,
        THORNS,
        CUSTOM
    }
}
