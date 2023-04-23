/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.events.player;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PlayerItemBreakEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    private final ItemStack brokenItem;

    public PlayerItemBreakEvent(AuroraMCServerPlayer player, ItemStack brokenItem) {
        super(player);
        this.brokenItem = brokenItem;
    }

    public ItemStack getBrokenItem() {
        return brokenItem;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
