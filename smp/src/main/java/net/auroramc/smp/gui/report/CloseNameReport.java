/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.gui.report;

import net.auroramc.api.player.PlayerReport;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import net.auroramc.smp.api.utils.gui.GUI;
import net.auroramc.smp.api.utils.gui.GUIItem;
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

        this.setItem(0, 4, new GUIItem(Material.PLAYER_HEAD, "&3&lUsername Report", 1, String.format(";&r&fReported Username: **%s**.", player.getActiveReport().getSuspectName()), (short)0, false, player.getActiveReport().getSuspectName()));

        this.setItem(1, 2, new GUIItem(Material.RED_CONCRETE, "&c&lInappropriate", 1, ";&r&fClick here to mark this;&r&fusername as &cInappropriate&r&f."));
        this.setItem(1, 6, new GUIItem(Material.GREEN_CONCRETE, "&a&lAppropriate", 1, ";&r&fClick here to mark this;&r&fusername as &aAppropriate&r&f."));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (item.getType() == Material.GRAY_STAINED_GLASS_PANE || item.getType() == Material.PLAYER_HEAD) {
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 100, 0);
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
