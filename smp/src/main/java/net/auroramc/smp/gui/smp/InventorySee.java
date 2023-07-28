/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.gui.smp;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import net.auroramc.smp.api.utils.gui.GUI;
import net.auroramc.smp.api.utils.gui.GUIItem;
import net.auroramc.smp.utils.InventoryUtil;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.logging.Level;

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
            AuroraMCAPI.getLogger().log(Level.WARNING, "An exception has occurred. Stack trace: ", e);
        }
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        return;
    }
}
