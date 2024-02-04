/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.api.events.block;

import net.auroramc.smp.api.events.player.PlayerEvent;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class BlockPlaceEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Block block;
    protected boolean canBuild;
    protected Block placedAgainst;
    protected BlockState replacedBlockState;
    protected ItemStack itemInHand;
    private boolean cancelled;

    public BlockPlaceEvent(AuroraMCServerPlayer player, Block placedBlock, BlockState replacedBlockState, Block placedAgainst, ItemStack itemInHand, boolean canBuild) {
        super(player);
        this.block = placedBlock;
        this.cancelled = false;
        this.placedAgainst = placedAgainst;
        this.itemInHand = itemInHand;
        this.replacedBlockState = replacedBlockState;
        this.canBuild = canBuild;
    }

    public Block getBlockPlaced() {
        return this.getBlock();
    }

    public BlockState getBlockReplacedState() {
        return this.replacedBlockState;
    }

    public Block getBlockAgainst() {
        return this.placedAgainst;
    }

    public ItemStack getItemInHand() {
        return this.itemInHand;
    }

    public boolean canBuild() {
        return this.canBuild;
    }

    public void setBuild(boolean canBuild) {
        this.canBuild = canBuild;
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
