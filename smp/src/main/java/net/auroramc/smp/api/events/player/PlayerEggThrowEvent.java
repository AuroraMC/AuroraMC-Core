/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.api.events.player;

import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerEggThrowEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private EntityType hatchingType;
    private final Egg egg;
    private boolean hatching;
    private byte numHatches;
    private boolean cancelled;

    public PlayerEggThrowEvent(AuroraMCServerPlayer player, Egg egg, boolean hatching, byte numHatches, EntityType hatchingType) {
        super(player);
        this.egg = egg;
        this.hatching = hatching;
        this.numHatches = numHatches;
        this.hatchingType = hatchingType;
        cancelled = false;
    }

    public Egg getEgg() {
        return egg;
    }

    public boolean isHatching() {
        return hatching;
    }

    public void setHatching(boolean hatching) {
        this.hatching = hatching;
    }

    public byte getNumHatches() {
        return numHatches;
    }

    public void setNumHatches(byte numHatches) {
        this.numHatches = numHatches;
    }

    public void setHatchingType(EntityType hatchingType) {
        this.hatchingType = hatchingType;
    }

    public EntityType getHatchingType() {
        return hatchingType;
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
