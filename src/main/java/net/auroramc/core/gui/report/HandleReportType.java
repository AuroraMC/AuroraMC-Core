package net.auroramc.core.gui.report;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.PlayerReport;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class HandleReportType extends GUI {

    private final AuroraMCPlayer player;

    public HandleReportType(AuroraMCPlayer player) {
        super("&3&lChoose a report type", 2, true);

        this.player = player;
        for (int i = 0; i <= 8; i++) {
            if (i < 3) {
                this.setItem(i, 0, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lChoose a report type", 1, "", (short) 7));
                this.setItem(i, 8, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lChoose a report type", 1, "", (short) 7));
            }
            this.setItem(0, i, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lChoose a report type", 1, "", (short) 7));
            this.setItem(2, i, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lChoose a report type", 1, "", (short) 7));
        }
        int column = 2;
        if (player.hasPermission("admin")) {
            column--;
            this.setItem(1, 7, new GUIItem(Material.NAME_TAG, "&3&lInappropriate Name Report", 1, ";&rClick here to handle an inappropriate username report."));
        }

        this.setItem(1, column, new GUIItem(Material.BOOK_AND_QUILL, "&3&lChat Report", 1, ";&rClick here to handle a chat report."));
        this.setItem(1, column + 2, new GUIItem(Material.SIGN, "&3&lMisc Report", 1, ";&rClick here to handle a misc report."));
        this.setItem(1, column + 4, new GUIItem(Material.IRON_SWORD, "&3&lHacking Report", 1, ";&rClick here to handle a hacking report."));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        switch (item.getType()) {
            case STAINED_GLASS_PANE:
                player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                break;
            case BOOK_AND_QUILL: {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (player.hasPermission("admin")) {
                            PlayerReport report = AuroraMCAPI.getDbManager().assignReport(player.getId(), PlayerReport.QueueType.LEADERSHIP, PlayerReport.ReportType.CHAT);
                            if (report == null) {
                                report = AuroraMCAPI.getDbManager().assignReport(player.getId(), PlayerReport.QueueType.NORMAL, PlayerReport.ReportType.CHAT);
                                if (report == null) {
                                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", "There are currently no chat reports in either report queue."));
                                    return;
                                }
                            }
                            player.setActiveReport(report);
                        } else {
                            PlayerReport report = AuroraMCAPI.getDbManager().assignReport(player.getId(), PlayerReport.QueueType.NORMAL, PlayerReport.ReportType.CHAT);
                            if (report == null) {
                                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", "There are currently no chat reports to handle."));
                                return;
                            }
                            player.setActiveReport(report);
                        }
                    }
                }.runTaskAsynchronously(AuroraMCAPI.getCore());
                player.getPlayer().closeInventory();
                break;
            }
            case SIGN: {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (player.hasPermission("admin")) {
                            PlayerReport report = AuroraMCAPI.getDbManager().assignReport(player.getId(), PlayerReport.QueueType.LEADERSHIP, PlayerReport.ReportType.MISC);
                            if (report == null) {
                                report = AuroraMCAPI.getDbManager().assignReport(player.getId(), PlayerReport.QueueType.NORMAL, PlayerReport.ReportType.MISC);
                                if (report == null) {
                                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", "There are currently no misc reports in either report queue."));
                                    return;
                                }
                            }
                            player.setActiveReport(report);
                        } else {
                            PlayerReport report = AuroraMCAPI.getDbManager().assignReport(player.getId(), PlayerReport.QueueType.NORMAL, PlayerReport.ReportType.MISC);
                            if (report == null) {
                                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", "There are currently no misc reports to handle."));
                                return;
                            }
                            player.setActiveReport(report);
                        }
                    }
                }.runTaskAsynchronously(AuroraMCAPI.getCore());
                player.getPlayer().closeInventory();
                break;
            }
            case IRON_SWORD: {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (player.hasPermission("admin")) {
                            PlayerReport report = AuroraMCAPI.getDbManager().assignReport(player.getId(), PlayerReport.QueueType.LEADERSHIP, PlayerReport.ReportType.HACKING);
                            if (report == null) {
                                report = AuroraMCAPI.getDbManager().assignReport(player.getId(), PlayerReport.QueueType.NORMAL, PlayerReport.ReportType.HACKING);
                                if (report == null) {
                                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", "There are currently no hacking reports in either report queue."));
                                    return;
                                }
                            }
                            player.setActiveReport(report);
                        } else {
                            PlayerReport report = AuroraMCAPI.getDbManager().assignReport(player.getId(), PlayerReport.QueueType.NORMAL, PlayerReport.ReportType.HACKING);
                            if (report == null) {
                                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", "There are currently no hacking reports to handle."));
                                return;
                            }
                            player.setActiveReport(report);
                        }
                    }
                }.runTaskAsynchronously(AuroraMCAPI.getCore());
                player.getPlayer().closeInventory();
                break;
            }
            case NAME_TAG: {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                            PlayerReport report = AuroraMCAPI.getDbManager().assignReport(player.getId(), PlayerReport.QueueType.LEADERSHIP, PlayerReport.ReportType.INAPPROPRIATE_NAME);
                            if (report == null) {
                                report = AuroraMCAPI.getDbManager().assignReport(player.getId(), PlayerReport.QueueType.NORMAL, PlayerReport.ReportType.INAPPROPRIATE_NAME);
                                if (report == null) {
                                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", "There are currently no inappropriate name reports in either report queue."));
                                    return;
                                }
                            }
                            player.setActiveReport(report);
                    }
                }.runTaskAsynchronously(AuroraMCAPI.getCore());
                player.getPlayer().closeInventory();
                break;
            }
        }
    }
}
