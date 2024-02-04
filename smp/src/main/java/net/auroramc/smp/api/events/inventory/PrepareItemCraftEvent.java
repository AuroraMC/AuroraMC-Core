/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.api.events.inventory;

import net.auroramc.smp.api.events.player.PlayerEvent;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public class PrepareItemCraftEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled;
    private final InventoryView view;
    private boolean repair;
    private CraftingInventory matrix;

    public PrepareItemCraftEvent(AuroraMCServerPlayer player, CraftingInventory what, InventoryView view, boolean isRepair) {
        super(player);
        this.view = view;
        this.matrix = what;
        this.repair = isRepair;
    }

    public Recipe getRecipe() {
        return this.matrix.getRecipe();
    }

    public CraftingInventory getInventory() {
        return this.matrix;
    }

    public boolean isRepair() {
        return this.repair;
    }

    public ItemStack getCursor() {
        return this.getView().getCursor();
    }

    public InventoryView getView() {
        return view;
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
