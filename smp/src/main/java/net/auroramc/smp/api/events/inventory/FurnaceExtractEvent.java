/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.api.events.inventory;

import net.auroramc.smp.api.events.player.PlayerEvent;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class FurnaceExtractEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Block block;
    private final Material itemType;
    private final int itemAmount;
    private boolean cancelled;
    private int exp;

    public FurnaceExtractEvent(AuroraMCServerPlayer player, Block block, Material itemType, int itemAmount, int exp) {
        super(player);
        this.block = block;
        this.itemType = itemType;
        this.itemAmount = itemAmount;
        this.exp = exp;
        cancelled = false;
    }

    public int getExpToDrop() {
        return this.exp;
    }

    public void setExpToDrop(int exp) {
        this.exp = exp;
    }

    public Material getItemType() {
        return this.itemType;
    }

    public int getItemAmount() {
        return this.itemAmount;
    }

    public Block getBlock() {
        return block;
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
