/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.core.gui.report;

import net.auroramc.api.player.PlayerReport;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class CloseNameReport extends GUI {

    private final AuroraMCServerPlayer player;

    public CloseNameReport(AuroraMCServerPlayer player) {
        super("&3&lClose Name Report", 2, true);

        this.player = player;

        fill("&r&f ", "");

        this.setItem(0, 4, new GUIItem(Material.SKULL_ITEM, "&3&lUsername Report", 1, String.format(";&r&fReported Username: **%s**.", player.getActiveReport().getSuspectName()), (short)3, false, player.getActiveReport().getSuspectName()));

        this.setItem(1, 2, new GUIItem(Material.STAINED_CLAY, "&c&lInappropriate", 1, ";&r&fClick here to mark this;&r&fusername as &cInappropriate&r&f.", (short)14));
        this.setItem(1, 6, new GUIItem(Material.STAINED_CLAY, "&a&lAppropriate", 1, ";&r&fClick here to mark this;&r&fusername as &aAppropriate&r&f.", (short)13));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (item.getType() == Material.STAINED_GLASS_PANE || item.getType() == Material.SKULL_ITEM) {
            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
            return;
        }

        if (item.getDurability() == 14) {
            PlayerReport report = player.getActiveReport();
            new BukkitRunnable(){
                @Override
                public void run() {
                    report.handle(player, PlayerReport.ReportOutcome.ACCEPTED, null, false);
                }
            }.runTaskAsynchronously(ServerAPI.getCore());
        } else {
            PlayerReport report = player.getActiveReport();

            new BukkitRunnable(){
                @Override
                public void run() {
                    report.handle(player, PlayerReport.ReportOutcome.DENIED, null, false);
                }
            }.runTaskAsynchronously(ServerAPI.getCore());
        }
        player.setActiveReport(null);
        player.closeInventory();
    }

}
