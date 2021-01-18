package net.auroramc.core.gui.report;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.PlayerReport;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.core.managers.ReportManager;
import org.apache.commons.lang.WordUtils;
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

        PlayerReport.ReportType type = PlayerReport.ReportType.CHAT;

        switch (item.getType()) {
            case STAINED_GLASS_PANE:
                player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
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
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", String.format("There are currently no %s reports in either report queue.", WordUtils.capitalizeFully(finalType.name().replace("_", " ")))));
                            if (autoHandled > 0) {
                                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", String.format("**%s** reports were automatically handled by the system.", autoHandled)));
                            }
                            return;
                        }
                    }
                    if (autoHandled > 0) {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", String.format("**%s** reports were automatically handled by the system.", autoHandled)));
                    }
                    player.setActiveReport(report);
                } else {
                    PlayerReport report = AuroraMCAPI.getDbManager().assignReport(player.getId(), PlayerReport.QueueType.NORMAL, finalType);
                    while (ReportManager.canAutoHandle(player, report)) {
                        autoHandled++;
                        report = AuroraMCAPI.getDbManager().assignReport(player.getId(), PlayerReport.QueueType.NORMAL, finalType);
                    }
                    if (report == null) {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", String.format("There are currently no %s reports to handle.", WordUtils.capitalizeFully(finalType.name().replace("_", " ")))));
                        if (autoHandled > 0) {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", String.format("**%s** reports were automatically handled by the system.", autoHandled)));
                        }
                        return;
                    }
                    if (autoHandled > 0) {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Reports", String.format("**%s** reports were automatically handled by the system.", autoHandled)));
                    }
                    player.setActiveReport(report);
                }
            }
        }.runTaskAsynchronously(AuroraMCAPI.getCore());
        player.getPlayer().closeInventory();
    }
}