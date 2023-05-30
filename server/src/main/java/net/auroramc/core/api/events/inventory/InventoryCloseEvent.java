/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.core.api.events.inventory;

import net.auroramc.core.api.events.player.PlayerEvent;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class InventoryCloseEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    private final InventoryView view;

    public InventoryCloseEvent(AuroraMCServerPlayer player, InventoryView view) {
        super(player);

        this.view = view;
    }

    public ItemStack getCursor() {
        return this.getView().getCursor();
    }

    public InventoryView getView() {
        return view;
    }

    public Inventory getInventory() {
        return this.view.getTopInventory();
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
