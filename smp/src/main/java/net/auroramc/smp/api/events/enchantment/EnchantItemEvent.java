/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.api.events.enchantment;

import net.auroramc.smp.api.events.player.PlayerEvent;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class EnchantItemEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Block table;
    private final InventoryView view;
    private final ItemStack item;
    private int level;
    private final Map<Enchantment, Integer> enchants;
    private final int button;

    private boolean cancelled;

    public EnchantItemEvent(AuroraMCServerPlayer player, InventoryView view, Block table, ItemStack item, int level, Map<Enchantment,Integer> enchants, int i) {
        super(player);
        this.table = table;
        this.view = view;
        this.enchants = enchants;
        this.item = item;
        this.level = level;
        this.button = i;
        cancelled = false;
    }

    public ItemStack getItem() {
        return item;
    }

    public Block getTable() {
        return table;
    }

    public Map<Enchantment, Integer> getEnchants() {
        return enchants;
    }

    public int whichButton() {
        return button;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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
