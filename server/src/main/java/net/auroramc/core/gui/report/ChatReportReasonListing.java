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
import net.auroramc.core.managers.ReportManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ChatReportReasonListing extends GUI {

    private final AuroraMCServerPlayer player;
    private final int id;
    private final String name;
    private int currentPage;
    private List<PlayerReport.ReportReason> reportReasons;
    private final PlayerReport.ChatType chatType;

    public ChatReportReasonListing(AuroraMCServerPlayer player, int id, String name, PlayerReport.ChatType type) {
        super(String.format("&3&lReport %s", name), 5, true);

        this.player = player;
        this.id = id;
        this.name = name;
        this.chatType = type;

        border(String.format("&3&lReport %s", name), "");

        this.setItem(0, 4, new GUIItem(Material.SKULL_ITEM, String.format("&3&lReport %s", name), 1, "&r&fPlease choose a reason.", (short)3, false, name));

        this.reportReasons = Arrays.stream(PlayerReport.ReportReason.values()).filter(reason -> reason.getType() == PlayerReport.ReportType.CHAT).sorted(Comparator.comparing(PlayerReport.ReportReason::getName)).collect(Collectors.toList());
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
            this.setItem(row, column, new GUIItem(Material.BOOK_AND_QUILL, "&3&l" + reason.getName(), 1, String.format(";&r&fClick here to report this;&r&fplayer for **%s**", reason.getName())));
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
        if (item.getType() == Material.GLASS || item.getType() == Material.SKULL_ITEM) {
            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
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
                this.updateItem(row, column, new GUIItem(Material.BOOK_AND_QUILL, "&3&l" + reason.getName(), 1, String.format(";&r&fClick here to report this;&r&fplayer for **%s**", reason.getName())));
            }
        } else {
            PlayerReport.ReportReason reason = reportReasons.get(((currentPage - 1) * 10) + ((row - 2) * 5) + (column - 2));
            new BukkitRunnable(){
                @Override
                public void run() {
                    ReportManager.newReport(id, name, player, System.currentTimeMillis(), PlayerReport.ReportType.CHAT, chatType, reason, ((player.hasPermission("moderation"))? PlayerReport.QueueType.LEADERSHIP:PlayerReport.QueueType.NORMAL));
                }
            }.runTaskAsynchronously(ServerAPI.getCore());
            player.closeInventory();
        }
    }

}
