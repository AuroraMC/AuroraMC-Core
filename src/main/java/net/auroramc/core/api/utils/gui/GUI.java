/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.utils.gui;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.exception.InvalidColumnException;
import net.auroramc.core.api.exception.InvalidRowException;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class GUI {

    private final int rows;
    private final String name;
    private final Map<Integer, GUIRow> inventory;
    private final boolean cancelEvent;
    private Inventory inv;

    public GUI(String name, int rows, boolean cancelEvent) {
        this.name = name;
        this.cancelEvent = cancelEvent;
        if (rows > 5 || rows < 0) {
            throw new InvalidRowException();
        }

        this.rows = rows;
        inventory = new HashMap<>();
        for (int i = 0;i <= rows;i++) {
            inventory.put(i, new GUIRow(i));
        }
    }

    public void setItem(int row, int column, GUIItem item) {
        if (row > 5 || row < 0 || row > rows) {
            throw new InvalidRowException();
        }

        if (column > 8 || column < 0) {
            throw new InvalidColumnException();
        }

        inventory.get(row).setItem(column, item);
    }

    public void border(String name, String lore) {
        GUIItem item = new GUIItem(Material.STAINED_GLASS_PANE, name, 1, lore, (short) 7);
        for (int i = 0; i <= 8; i++) {
            if (i <= rows) {
                this.setItem(i, 0, item);
                this.setItem(i, 8, item);
            }
            this.setItem(0, i, item);
            this.setItem(rows, i, item);
        }
    }

    public void fill(String name, String lore) {
        for (int x = 0;x <= 8;x++) {
            for (int y = 0;y<=rows;y++) {
                this.setItem(y, x, new GUIItem(Material.STAINED_GLASS_PANE, name, 1, lore, (short) 7));
            }
        }
    }

    public void updateItem(int row, int column, GUIItem item) {
        setItem(row, column, item);
        if (item == null) {
            inv.clear((row * 9) + column);
        } else {
            inv.setItem((row * 9) + column, item.getItem());
        }

    }

    public void open(AuroraMCPlayer player) {
        Inventory inventory = Bukkit.createInventory(null, (rows + 1) * 9, AuroraMCAPI.getFormatter().convert(name));
        for (int row = 0;row <= rows;row++) {
            for (int column = 0;column <=8;column++) {
                if (this.inventory.get(row).getItem(column) != null) {
                    inventory.setItem((row * 9) + column, this.inventory.get(row).getItem(column).getItem());
                }
            }
        }
        player.getPlayer().openInventory(inventory);
        inv = inventory;
    }

    public int getSize() {
        return rows * 9;
    }

    public abstract void onClick(int row, int column, ItemStack item, ClickType clickType);

    public boolean cancelEvent() {
        return cancelEvent;
    }

}
