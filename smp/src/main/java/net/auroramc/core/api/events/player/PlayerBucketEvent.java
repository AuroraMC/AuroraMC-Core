/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.events.player;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PlayerBucketEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Block blockClicked;
    private final BlockFace blockFace;
    private final Material bucket;
    private ItemStack itemInHand;
    private boolean cancelled;

    public PlayerBucketEvent(AuroraMCServerPlayer player, Block blockClicked, BlockFace blockFace, Material bucket, ItemStack itemInHand) {
        super(player);
        this.blockClicked = blockClicked;
        this.blockFace = blockFace;
        this.bucket = bucket;
        this.itemInHand = itemInHand;
        cancelled = false;
    }

    public Block getBlockClicked() {
        return blockClicked;
    }

    public Material getBucket() {
        return bucket;
    }

    public BlockFace getBlockFace() {
        return blockFace;
    }

    public ItemStack getItemInHand() {
        return itemInHand;
    }

    public void setItemInHand(ItemStack itemInHand) {
        this.itemInHand = itemInHand;
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
