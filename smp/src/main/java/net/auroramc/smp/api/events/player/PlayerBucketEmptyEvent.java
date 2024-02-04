/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.api.events.player;

import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PlayerBucketEmptyEvent extends PlayerBucketEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled;

    public PlayerBucketEmptyEvent(AuroraMCServerPlayer player, Block blockClicked, BlockFace blockFace, Material bucket, ItemStack itemInHand) {
        super(player, blockClicked, blockFace, bucket, itemInHand);
        cancelled = false;
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
