/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
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

public class EnderChestSee extends GUI {


    public EnderChestSee(AuroraMCServerPlayer player, AuroraMCServerPlayer player2) {
        super("&3&l"  + player2.getByDisguiseName() + "'s Ender Chest", 5, true);
        int column = 0;
        int row = 0;
        for (ItemStack stack : player2.getEnderChest().getContents()) {
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

    public EnderChestSee(AuroraMCServerPlayer player, String[] items, String name) {
        super("&3&l"  + name + "'s Ender Chest", 5, true);
        int column = 0;
        int row = 0;
        try {
            for (ItemStack stack : InventoryUtil.itemStackArrayFromBase64(items[4])) {
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
