/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.api.events.inventory;

import net.auroramc.smp.api.events.player.PlayerEvent;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class InventoryOpenEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled;
    private final InventoryView view;

    public InventoryOpenEvent(AuroraMCServerPlayer player, InventoryView view) {
        super(player);
        cancelled = false;

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

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

}
