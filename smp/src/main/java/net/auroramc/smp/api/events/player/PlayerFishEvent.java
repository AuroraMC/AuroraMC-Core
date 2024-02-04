/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.api.events.player;

import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerFishEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Entity entity;
    private boolean cancelled;
    private int exp;
    private final State state;
    private final FishHook hookEntity;

    public PlayerFishEvent(AuroraMCServerPlayer player, Entity entity, FishHook hookEntity, State state) {
        super(player);
        this.entity = entity;
        this.hookEntity = hookEntity;
        this.state = state;
        cancelled = false;
    }

    public int getExpToDrop() {
        return this.exp;
    }

    public void setExpToDrop(int amount) {
        this.exp = amount;
    }

    public State getState() {
        return this.state;
    }

    public Entity getCaught() {
        return this.entity;
    }

    public FishHook getHook() {
        return this.hookEntity;
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

    public static enum State {
        FISHING,
        CAUGHT_FISH,
        CAUGHT_ENTITY,
        IN_GROUND,
        FAILED_ATTEMPT
    }
}
