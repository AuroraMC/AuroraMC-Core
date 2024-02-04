/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.core.api.events.block;

import net.auroramc.core.api.events.player.PlayerEvent;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class BlockDamageEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Block block;
    private final ItemStack itemInHand;
    private boolean instaBreak;
    private boolean cancelled;

    public BlockDamageEvent(AuroraMCServerPlayer player, Block block, ItemStack itemInHand, boolean instaBreak) {
        super(player);
        this.block = block;
        this.itemInHand = itemInHand;
        this.instaBreak = instaBreak;
        this.cancelled = false;
    }

    public void setInstaBreak(boolean instaBreak) {
        this.instaBreak = instaBreak;
    }

    public boolean isInstaBreak() {
        return instaBreak;
    }

    public ItemStack getItemInHand() {
        return itemInHand;
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
