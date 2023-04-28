/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.managers;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.events.inventory.InventoryClickEvent;
import net.auroramc.core.api.events.inventory.InventoryCloseEvent;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

public class GUIManager implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        AuroraMCServerPlayer player = e.getPlayer();
        if (ServerAPI.getGUI(player) != null) {
            ServerAPI.closeGUI(player);
            player.closeInventory();
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        GUI gui = ServerAPI.getGUI(e.getPlayer());
        if (gui != null) {
            if (e.getClickedInventory() instanceof PlayerInventory) {
                return;
            }
            if (e.getSlot() < 0 || e.getSlot() >= e.getInventory().getSize()) {
                return;
            }
            if (e.getInventory() == null) {
                return;
            }
            ItemStack itemStack = e.getClickedInventory().getItem(e.getSlot());
            if (e.getCursor() != null && itemStack == null) {
                itemStack = e.getCursor();
            }
            if (itemStack != null) {
                if (itemStack.getType() != Material.AIR) {
                    if (e.getInventory().getType() != InventoryType.PLAYER && e.getInventory().getType() != InventoryType.CREATIVE) {
                        if (gui.cancelEvent()) {
                            e.setCancelled(true);

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    e.getPlayer().updateInventory();
                                }
                            }.runTaskLater(ServerAPI.getCore(), 3);
                        }
                        int row = e.getSlot() / 9;
                        int column = e.getSlot() % 9;
                        gui.onClick(row, column, itemStack, e.getClick());
                    }
                }
            }
        }
    }

}
