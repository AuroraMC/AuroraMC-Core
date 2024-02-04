/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.core.gui.report;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.player.PlayerReport;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.core.managers.ReportManager;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class HandleReportType extends GUI {

    private final AuroraMCServerPlayer player;

    public HandleReportType(AuroraMCServerPlayer player) {
        super("&3&lChoose a report type", 2, true);

        this.player = player;
        border("&3&lChoose a report type", "");
        int column = 2;
        if (player.hasPermission("admin")) {
            column--;
            this.setItem(1, 7, new GUIItem(Material.NAME_TAG, "&3&lInappropriate Name Report", 1, ";&r&fClick here to handle an inappropriate username report."));
        }

        this.setItem(1, column, new GUIItem(Material.BOOK_AND_QUILL, "&3&lChat Report", 1, ";&r&fClick here to handle a chat report."));
        this.setItem(1, column + 2, new GUIItem(Material.SIGN, "&3&lMisc Report", 1, ";&r&fClick here to handle a misc report."));
        this.setItem(2, 4, new GUIItem(Material.PAPER, "&3&lAny Report", 1, ";&r&fClick here to handle any open report."));
        GUIItem guiItem = new GUIItem(Material.IRON_SWORD, "&3&lHacking Report", 1, ";&r&fClick here to handle a hacking report.");
        ItemStack itemStack = guiItem.getItemStack();
        ItemMeta meta = itemStack.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(meta);
        this.setItem(1, column + 4, new GUIItem(itemStack));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {

        PlayerReport.ReportType type = PlayerReport.ReportType.CHAT;

        switch (item.getType()) {
            case STAINED_GLASS_PANE:
                player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                return;
            case SIGN:
                type = PlayerReport.ReportType.MISC;
                break;
            case IRON_SWORD:
                type = PlayerReport.ReportType.HACKING;
                break;
            case NAME_TAG:
                type = PlayerReport.ReportType.INAPPROPRIATE_NAME;
                break;
            case PAPER:{
                type = null;
                break;
            }
        }

        PlayerReport.ReportType finalType = type;

        new BukkitRunnable() {
            @Override
            public void run() {
                int autoHandled = 0;

                if (player.hasPermission("admin")) {
                    PlayerReport report = AuroraMCAPI.getDbManager().assignReport(player.getId(), PlayerReport.QueueType.LEADERSHIP, finalType);

                    while (ReportManager.canAutoHandle(player, report)) {
                        autoHandled++;
                        report = AuroraMCAPI.getDbManager().assignReport(player.getId(), PlayerReport.QueueType.LEADERSHIP, finalType);
                    }

                    if (report == null) {
                        report = AuroraMCAPI.getDbManager().assignReport(player.getId(), PlayerReport.QueueType.NORMAL, finalType);
                        while (ReportManager.canAutoHandle(player, report)) {
                            autoHandled++;
                            report = AuroraMCAPI.getDbManager().assignReport(player.getId(), PlayerReport.QueueType.NORMAL, finalType);
                        }
                        if (report == null) {
                            if (finalType == null) {
                                player.sendMessage(TextFormatter.pluginMessage("Reports", "There are currently no reports in either report queue."));
                            } else {
                                player.sendMessage(TextFormatter.pluginMessage("Reports", String.format("There are currently no %s reports in either report queue.", WordUtils.capitalizeFully(finalType.name().replace("_", " ")))));
                            }
                            if (autoHandled > 0) {
                                player.sendMessage(TextFormatter.pluginMessage("Reports", String.format("**%s** reports were automatically handled by the system.", autoHandled)));
                            }
                            return;
                        }
                    }
                    if (autoHandled > 0) {
                        player.sendMessage(TextFormatter.pluginMessage("Reports", String.format("**%s** reports were automatically handled by the system.", autoHandled)));
                    }
                    player.setActiveReport(report);
                } else {
                    PlayerReport report = AuroraMCAPI.getDbManager().assignReport(player.getId(), PlayerReport.QueueType.NORMAL, finalType);
                    while (ReportManager.canAutoHandle(player, report)) {
                        autoHandled++;
                        report = AuroraMCAPI.getDbManager().assignReport(player.getId(), PlayerReport.QueueType.NORMAL, finalType);
                    }
                    if (report == null) {
                        if (finalType == null) {
                            player.sendMessage(TextFormatter.pluginMessage("Reports", "There are currently no reports to handle."));
                        } else {
                            player.sendMessage(TextFormatter.pluginMessage("Reports", String.format("There are currently no %s reports to handle.", WordUtils.capitalizeFully(finalType.name().replace("_", " ")))));
                        }
                        if (autoHandled > 0) {
                            player.sendMessage(TextFormatter.pluginMessage("Reports", String.format("**%s** reports were automatically handled by the system.", autoHandled)));
                        }
                        return;
                    }
                    if (autoHandled > 0) {
                        player.sendMessage(TextFormatter.pluginMessage("Reports", String.format("**%s** reports were automatically handled by the system.", autoHandled)));
                    }
                    player.setActiveReport(report);
                }
            }
        }.runTaskAsynchronously(ServerAPI.getCore());
        player.closeInventory();
    }
}
