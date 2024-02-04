/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.core.api.events.player;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    protected ItemStack item;
    protected Action action;
    protected Block blockClicked;
    protected BlockFace blockFace;
    private Event.Result useClickedBlock;
    private Event.Result useItemInHand;
    private boolean cancelled;

    public PlayerInteractEvent(AuroraMCServerPlayer player, Action action, ItemStack item, Block clickedBlock, BlockFace clickedFace) {
        super(player);
        this.action = action;
        this.item = item;
        this.blockClicked = clickedBlock;
        this.blockFace = clickedFace;
        this.useItemInHand = Result.DEFAULT;
        this.useClickedBlock = clickedBlock == null ? Result.DENY : Result.ALLOW;
        cancelled = false;
    }

    public Action getAction() {
        return this.action;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public Material getMaterial() {
        return !this.hasItem() ? Material.AIR : this.item.getType();
    }

    public boolean hasBlock() {
        return this.blockClicked != null;
    }

    public boolean hasItem() {
        return this.item != null;
    }

    public boolean isBlockInHand() {
        return !this.hasItem() ? false : this.item.getType().isBlock();
    }

    public Block getClickedBlock() {
        return this.blockClicked;
    }

    public BlockFace getBlockFace() {
        return this.blockFace;
    }

    public Event.Result useInteractedBlock() {
        return this.useClickedBlock;
    }

    public void setUseInteractedBlock(Event.Result useInteractedBlock) {
        this.useClickedBlock = useInteractedBlock;
    }

    public Event.Result useItemInHand() {
        return this.useItemInHand;
    }

    public void setUseItemInHand(Event.Result useItemInHand) {
        this.useItemInHand = useItemInHand;
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
