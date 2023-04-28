/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.events.inventory;

import com.google.common.collect.ImmutableSet;
import net.auroramc.core.api.events.player.PlayerEvent;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.apache.commons.lang.Validate;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.DragType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class InventoryDragEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled;
    private final DragType type;
    private final Map<Integer, ItemStack> addedItems;
    private final Set<Integer> containerSlots;
    private final ItemStack oldCursor;
    private ItemStack newCursor;
    private final InventoryView view;

    public InventoryDragEvent(AuroraMCServerPlayer player, InventoryView what, ItemStack newCursor, ItemStack oldCursor, boolean right, Map<Integer, ItemStack> slots) {
        super(player);
        this.view = what;
        cancelled = false;
        Validate.notNull(oldCursor);
        Validate.notNull(slots);
        this.type = right ? DragType.SINGLE : DragType.EVEN;
        this.newCursor = newCursor;
        this.oldCursor = oldCursor;
        this.addedItems = slots;
        ImmutableSet.Builder<Integer> b = ImmutableSet.builder();
        Iterator var8 = slots.keySet().iterator();

        while(var8.hasNext()) {
            Integer slot = (Integer)var8.next();
            b.add(what.convertSlot(slot));
        }

        this.containerSlots = b.build();
    }

    public Inventory getInventory() {
        return this.view.getTopInventory();
    }

    public Map<Integer, ItemStack> getNewItems() {
        return Collections.unmodifiableMap(this.addedItems);
    }

    public Set<Integer> getRawSlots() {
        return this.addedItems.keySet();
    }

    public Set<Integer> getInventorySlots() {
        return this.containerSlots;
    }

    public ItemStack getCursor() {
        return this.newCursor;
    }

    public void setCursor(ItemStack newCursor) {
        this.newCursor = newCursor;
    }

    public ItemStack getOldCursor() {
        return this.oldCursor.clone();
    }

    public DragType getType() {
        return this.type;
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
