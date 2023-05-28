/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.gui.smp;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.core.utils.InventoryUtil;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

public class InventorySee extends GUI {


    public InventorySee(AuroraMCServerPlayer player, AuroraMCServerPlayer player2) {
        super("&3&l"  + player2.getByDisguiseName() + "'s Inventory", 5, true);
        int column = 0;
        int row = 0;
        for (ItemStack stack : player2.getInventory().getContents()) {
            this.setItem(row, column, new GUIItem(stack));
            column++;
            if (column == 9) {
                column = 0;
                row++;
                if (row > 5) {
                    return;
                }
            }
        }
    }

    public InventorySee(AuroraMCServerPlayer player, String[] items, String name) {
        super("&3&l"  + name + "'s Inventory", 5, true);
        int column = 0;
        int row = 0;
        try {
            for (ItemStack stack : InventoryUtil.itemStackArrayFromBase64(items[0])) {
                this.setItem(row, column, new GUIItem(stack));
                column++;
                if (column == 9) {
                    column = 0;
                    row++;
                    if (row > 5) {
                        return;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        return;
    }
}
