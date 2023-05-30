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
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ChangeReportReasonListing extends GUI {

    private final AuroraMCServerPlayer player;
    private final PlayerReport.ReportType type;
    private int currentPage;
    private final List<PlayerReport.ReportReason> reportReasons;

    public ChangeReportReasonListing(AuroraMCServerPlayer player, PlayerReport.ReportType type) {
        super("&3&lChange Report Reason", 5, true);

        this.player = player;
        this.type = type;

        border("&3&lChange Report Reason", "");

        this.setItem(0, 4, new GUIItem(Material.PLAYER_HEAD, "&3&lChange Report Reason", 1, "&r&fPlease choose a reason.", (short)0, false, player.getActiveReport().getSuspectName()));

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

            GUIItem guiItem = new GUIItem(((type == PlayerReport.ReportType.MISC)?Material.OAK_SIGN:((type == PlayerReport.ReportType.CHAT)?Material.WRITABLE_BOOK:Material.IRON_SWORD)), "&3&l" + reason.getName(), 1, String.format(";&r&fClick here to accept this;&r&freport as **%s**", reason.getName()));
            ItemStack itemStack = guiItem.getItemStack();
            ItemMeta meta = itemStack.getItemMeta();
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            itemStack.setItemMeta(meta);
            this.setItem(row, column, new GUIItem(itemStack));
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
        if (item.getType() == Material.GRAY_STAINED_GLASS_PANE || item.getType() == Material.PLAYER_HEAD) {
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 100, 0);
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
                GUIItem guiItem = new GUIItem(((type == PlayerReport.ReportType.MISC)?Material.OAK_SIGN:((type == PlayerReport.ReportType.CHAT)?Material.WRITABLE_BOOK:Material.IRON_SWORD)), "&3&l" + reason.getName(), 1, String.format(";&r&fClick here to report this;&r&fplayer for **%s**", reason.getName()));
                ItemStack itemStack = guiItem.getItemStack();
                ItemMeta meta = itemStack.getItemMeta();
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                itemStack.setItemMeta(meta);
                this.updateItem(row, column, new GUIItem(itemStack));
            }
        } else {
            PlayerReport.ReportReason reason = reportReasons.get(((currentPage - 1) * 10) + ((row - 2) * 5) + (column - 2));
            if (reason.getAltRule() != null) {
                ChangeReportReasonChooseRule chooseRule = new ChangeReportReasonChooseRule(player, reason);
                chooseRule.open(player);
            } else {
                PlayerReport report = player.getActiveReport();
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        report.handle(player, PlayerReport.ReportOutcome.ACCEPTED, reason, false);
                    }
                }.runTaskAsynchronously(ServerAPI.getCore());
                player.setActiveReport(null);
                player.closeInventory();
            }
        }
    }
}
