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
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class InventoryClickEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled;
    private final ClickType click;
    private final InventoryAction action;
    private final Inventory clickedInventory;
    private final InventoryView view;
    private final InventoryType.SlotType slot_type;
    private int whichSlot;
    private int rawSlot;
    private ItemStack current;
    private int hotbarKey;

    public InventoryClickEvent(AuroraMCServerPlayer player, InventoryView view, InventoryType.SlotType type, int slot, ClickType click, InventoryAction action, int key) {
        super(player);
        cancelled = false;

        this.current = null;
        this.hotbarKey = key;
        this.slot_type = type;
        this.rawSlot = slot;
        this.view = view;
        if (slot < 0) {
            this.clickedInventory = null;
        } else if (view.getTopInventory() != null && slot < view.getTopInventory().getSize()) {
            this.clickedInventory = view.getTopInventory();
        } else {
            this.clickedInventory = view.getBottomInventory();
        }

        this.whichSlot = view.convertSlot(slot);
        this.click = click;
        this.action = action;
    }

    public Inventory getClickedInventory() {
        return this.clickedInventory;
    }

    public InventoryType.SlotType getSlotType() {
        return this.slot_type;
    }

    public ItemStack getCursor() {
        return this.getView().getCursor();
    }

    public ItemStack getCurrentItem() {
        return this.slot_type == InventoryType.SlotType.OUTSIDE ? this.current : this.getView().getItem(this.rawSlot);
    }

    public boolean isRightClick() {
        return this.click.isRightClick();
    }

    public boolean isLeftClick() {
        return this.click.isLeftClick();
    }

    public boolean isShiftClick() {
        return this.click.isShiftClick();
    }

    public void setCurrentItem(ItemStack stack) {
        if (this.slot_type == InventoryType.SlotType.OUTSIDE) {
            this.current = stack;
        } else {
            this.getView().setItem(this.rawSlot, stack);
        }

    }

    public InventoryView getView() {
        return view;
    }

    public Inventory getInventory() {
        return this.view.getTopInventory();
    }

    public int getSlot() {
        return this.whichSlot;
    }

    public int getRawSlot() {
        return this.rawSlot;
    }

    public int getHotbarButton() {
        return this.hotbarKey;
    }

    public InventoryAction getAction() {
        return this.action;
    }

    public ClickType getClick() {
        return this.click;
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
