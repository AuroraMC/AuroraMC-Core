/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.core.api.events.player;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fish;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerFishEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Entity entity;
    private boolean cancelled;
    private int exp;
    private final PlayerFishEvent.State state;
    private final Fish hookEntity;

    public PlayerFishEvent(AuroraMCServerPlayer player, Entity entity, Fish hookEntity, PlayerFishEvent.State state) {
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

    public PlayerFishEvent.State getState() {
        return this.state;
    }

    public Entity getCaught() {
        return this.entity;
    }

    public Fish getHook() {
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
