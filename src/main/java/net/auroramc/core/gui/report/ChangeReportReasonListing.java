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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ChangeReportReasonListing extends GUI {

    private final AuroraMCPlayer player;
    private final PlayerReport.ReportType type;
    private int currentPage;
    private final List<PlayerReport.ReportReason> reportReasons;

    public ChangeReportReasonListing(AuroraMCPlayer player, PlayerReport.ReportType type) {
        super("&3&lChange Report Reason", 5, true);

        this.player = player;
        this.type = type;

        for (int i = 0; i <= 8; i++) {
            if (i < 6) {
                this.setItem(i, 0, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lChange Report Reason", 1, "", (short) 7));
                this.setItem(i, 8, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lChange Report Reason", 1, "", (short) 7));
            }
            this.setItem(0, i, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lChange Report Reason", 1, "", (short) 7));
            this.setItem(5, i, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lChange Report Reason", 1, "", (short) 7));
        }

        this.setItem(0, 4, new GUIItem(Material.SKULL_ITEM, "&3&lChange Report Reason", 1, "&rPlease choose a reason.", (short)3, false, player.getActiveReport().getSuspectName()));

        this.reportReasons = Arrays.stream(PlayerReport.ReportReason.values()).filter(reason -> reason.getType() == type).collect(Collectors.toList());
        if (reportReasons.size() > 10) {
            this.setItem(5, 7, new GUIItem(Material.ARROW, "&£&lNext Page"));
        }

        int column = 2;
        int row = 2;
        currentPage = 1;
        for (int i = 0;i < 10;i++) {

            int pi = (((currentPage - 1) * 10) + i);
            if (pi >= reportReasons.size()) {
                break;
            }
            PlayerReport.ReportReason reason = reportReasons.get(pi);

            this.setItem(row, column, new GUIItem(((type == PlayerReport.ReportType.MISC)?Material.SIGN:((type == PlayerReport.ReportType.CHAT)?Material.BOOK_AND_QUILL:Material.IRON_SWORD)), "&3&l" + reason.getName(), 1, String.format(";&rClick here to accept this;&rreport as **%s**", reason.getName())));
            column++;
            if (column == 7) {
                row++;
                column = 2;
                if (row == 4) {
                    break;
                }
            }
        }


    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (item.getType() == Material.STAINED_GLASS_PANE || item.getType() == Material.SKULL_ITEM) {
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
            return;
        }

        if (item.getType() == Material.ARROW) {
            if (column == 7) {
                currentPage++;
            } else {
                currentPage--;
            }

            column = 2;
            row = 2;
            for (int i = 0;i < 10;i++) {

                int pi = (((currentPage - 1) * 10) + i);
                if (reportReasons.size() <= pi) {
                    this.updateItem(row, column, null);
                    column++;
                    if (column == 7) {
                        row++;
                        column = 2;
                        if (row == 4) {
                            break;
                        }
                    }
                    continue;
                }

                PlayerReport.ReportReason reason = reportReasons.get(pi);
                this.setItem(row, column, new GUIItem(((type == PlayerReport.ReportType.MISC)?Material.SIGN:((type == PlayerReport.ReportType.CHAT)?Material.BOOK_AND_QUILL:Material.IRON_SWORD)), "&3&l" + reason.getName(), 1, String.format(";&rClick here to report this;&rplayer for **%s**", reason.getName())));
            }
        } else {
            PlayerReport.ReportReason reason = reportReasons.get(((currentPage - 1) * 10) + ((row - 2) * 5) + (column - 2));
            if (reason.getAltRule() != null) {
                ChangeReportReasonChooseRule chooseRule = new ChangeReportReasonChooseRule(player, reason);
                chooseRule.open(player);
                AuroraMCAPI.openGUI(player, chooseRule);
            } else {
                PlayerReport report = player.getActiveReport();
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        report.handle(player, PlayerReport.ReportOutcome.ACCEPTED, reason, false);
                    }
                }.runTaskAsynchronously(AuroraMCAPI.getCore());
                player.setActiveReport(null);
                player.getPlayer().closeInventory();
            }
        }
    }
}
