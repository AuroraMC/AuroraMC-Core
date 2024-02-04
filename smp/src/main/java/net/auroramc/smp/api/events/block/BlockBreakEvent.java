/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.api.events.block;

import net.auroramc.smp.api.events.player.PlayerEvent;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class BlockBreakEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Block block;
    private boolean cancelled;

    public BlockBreakEvent(AuroraMCServerPlayer player, Block block) {
        super(player);
        this.block = block;
        cancelled = false;
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
}
