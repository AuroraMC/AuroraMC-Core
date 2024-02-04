/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.core.api.events.enchantment;

import net.auroramc.core.api.events.player.PlayerEvent;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class PrepareItemEnchantEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Block table;
    private final InventoryView view;
    private final ItemStack item;
    private int bonus;
    private int[] levelsOffered;
    private boolean cancelled;

    public PrepareItemEnchantEvent(AuroraMCServerPlayer player, InventoryView view, Block table, ItemStack item, int[] levelsOffered, int bonus) {
        super(player);
        this.table = table;
        this.view = view;
        this.item = item;
        this.bonus = bonus;
        cancelled = false;
    }

    public int getEnchantmentBonus() {
        return bonus;
    }

    public int[] getExpLevelCostsOffered() {
        return levelsOffered;
    }

    public ItemStack getItem() {
        return item;
    }

    public Block getTable() {
        return table;
    }


    public InventoryView getView() {
        return view;
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
