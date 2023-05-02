/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.events.block;

import net.auroramc.core.api.events.player.PlayerEvent;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class BlockIgniteEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Block block;
    private boolean cancelled;
    private final IgniteCause cause;

    public BlockIgniteEvent(AuroraMCServerPlayer player, Block block, IgniteCause cause) {
        super(player);
        this.block = block;
        this.cause = cause;
        this.cancelled = false;
    }

    public IgniteCause getCause() {
        return cause;
    }

    public Block getBlock() {
        return block;
    }

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

    public static enum IgniteCause {
        LAVA,
        FLINT_AND_STEEL,
        SPREAD,
        LIGHTNING,
        FIREBALL,
        ENDER_CRYSTAL,
        EXPLOSION
    }

}
