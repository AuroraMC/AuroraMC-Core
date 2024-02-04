/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.core.api.events.inventory;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.*;

public class CraftItemEvent extends InventoryClickEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Recipe recipe;

    public CraftItemEvent(AuroraMCServerPlayer player, Recipe recipe, InventoryView view, InventoryType.SlotType type, int slot, ClickType click, InventoryAction action, int key) {
        super(player, view, type, slot, click, action, key);

        this.recipe = recipe;
    }

    public Recipe getRecipe() {
        return this.recipe;
    }

    public CraftingInventory getInventory() {
        return (CraftingInventory)super.getInventory();
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
