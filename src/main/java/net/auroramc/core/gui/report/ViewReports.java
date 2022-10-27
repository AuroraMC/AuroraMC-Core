/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.gui.report;

import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.PlayerReport;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Date;
import java.util.List;

public class ViewReports extends GUI {

    private final AuroraMCPlayer player;
    private final List<PlayerReport> reports;
    private int currentPage;

    public ViewReports(AuroraMCPlayer player, List<PlayerReport> reports) {
        super("&3&lYour Reports", 5, true);

        this.player = player;
        this.reports = reports;

        border("&3&lYour Reports", "");

        this.setItem(0, 4, new GUIItem(Material.SKULL_ITEM, "&3&lYour Reports", 1, ";&r&fView all reports you have submitted.", (short)3, false, player.getName()));

        if (reports.size() > 28) {
            this.setItem(5, 7, new GUIItem(Material.ARROW, "&3&lNext Page"));
        }
        int row = 1;
        int column = 1;
        for (PlayerReport report : reports) {
            Material item = Material.BOOK_AND_QUILL;
            String name = "&3&lChat Report";
            switch (report.getType()) {
                case HACKING:
                    item = Material.IRON_SWORD;
                    name = "&3&lHacking Report";
                    break;
                case MISC:
                    item = Material.SIGN;
                    name = "&3&lMisc Report";
                    break;
                case INAPPROPRIATE_NAME:
                    item = Material.NAME_TAG;
                    name = "&3&lInappropriate Name";
                    break;
            }

            String lore;
            if (report.getType() == PlayerReport.ReportType.INAPPROPRIATE_NAME) {
                lore = String.format("&r&fPlayer Reported: **%s**;&r&fDate: **%s**;&r&f ;&r&fOutcome: %s", report.getSuspectName(), new Date(report.getTimestamp()), ((report.getOutcome() == PlayerReport.ReportOutcome.PENDING)?"&6Pending":((report.getOutcome() == PlayerReport.ReportOutcome.ACCEPTED)?"&aAccepted":"&cDenied")));
            } else {
                lore = String.format("&r&fPlayer Reported: **%s**;&r&fReason: **%s**;&r&fDate: **%s**;&r&f ;&r&fOutcome: %s", report.getSuspectName(), report.getReason().getName(), new Date(report.getTimestamp()), ((report.getOutcome() == PlayerReport.ReportOutcome.PENDING)?"&6Pending":((report.getOutcome() == PlayerReport.ReportOutcome.ACCEPTED)?"&aAccepted":"&cDenied")));
            }

            if (report.getOutcome() == PlayerReport.ReportOutcome.ACCEPTED && report.getType() != PlayerReport.ReportType.INAPPROPRIATE_NAME && report.getReason() != report.getReasonAccepted()) {
                lore = String.format("%s;&r&fReason Accepted: **%s**", lore, report.getReasonAccepted().getName());
            }

            GUIItem guiItem = new GUIItem(item, name, 1, lore);
            ItemStack itemStack = guiItem.getItem();
            ItemMeta meta = itemStack.getItemMeta();
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            itemStack.setItemMeta(meta);
            this.setItem(row, column, new GUIItem(itemStack));

            column++;
            if (column == 8) {
                row++;
                column = 1;
                if (row == 5) {
                    break;
                }
            }

        }
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (item.getType() == Material.ARROW) {
            //Go to next/previous page.
            if (column == 1) {
                //Prev page.
                currentPage--;
                this.updateItem(5, 7, new GUIItem(Material.ARROW, "&3&lNext Page"));

                if (currentPage == 1) {
                    this.updateItem(5, 1, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lYour Reports", 1, "", (short) 7));
                }
            } else {
                //next page.
                currentPage++;
                if (reports.size() < ((currentPage) * 28)) {
                    this.updateItem(5, 7, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lYour Reports", 1, "", (short) 7));
                }

                this.updateItem(5, 1, new GUIItem(Material.ARROW, "&3&lPrevious Page"));
            }
            column = 1;
            row = 1;
            for (int i = 0;i < 28;i++) {
                //show the prev 28 items.

                int pi = (((currentPage - 1) * 28) + i);
                if (reports.size() <= pi) {
                    this.updateItem(row, column, null);
                    column++;
                    if (column == 8) {
                        row++;
                        column = 1;
                        if (row == 5) {
                            break;
                        }
                    }
                    continue;
                }

                PlayerReport report = reports.get(pi);

                Material guiitem = Material.BOOK_AND_QUILL;
                String name = "&3&lChat Report";
                switch (report.getType()) {
                    case HACKING:
                        guiitem = Material.IRON_SWORD;
                        name = "&3&lHacking Report";
                        break;
                    case MISC:
                        guiitem = Material.SIGN;
                        name = "&3&lMisc Report";
                        break;
                    case INAPPROPRIATE_NAME:
                        guiitem = Material.NAME_TAG;
                        name = "&3&lInappropriate Name Report";
                        break;
                }

                String lore;
                if (report.getType() == PlayerReport.ReportType.INAPPROPRIATE_NAME) {
                    lore = String.format("&r&fPlayer Reported: **%s**;&r&fDate: **%s**;&r&f ;&r&fOutcome: %s", report.getSuspectName(), new Date(report.getTimestamp()), ((report.getOutcome() == PlayerReport.ReportOutcome.PENDING)?"&6Pending":((report.getOutcome() == PlayerReport.ReportOutcome.ACCEPTED)?"&aAccepted":"&cDenied")));
                } else {
                    lore = String.format("&r&fPlayer Reported: **%s**;&r&fReason: **%s**;&r&fDate: **%s**;&r&f ;&r&fOutcome: %s", report.getSuspectName(), report.getReason().getName(), new Date(report.getTimestamp()), ((report.getOutcome() == PlayerReport.ReportOutcome.PENDING)?"&6Pending":((report.getOutcome() == PlayerReport.ReportOutcome.ACCEPTED)?"&aAccepted":"&cDenied")));
                }

                if (report.getOutcome() == PlayerReport.ReportOutcome.ACCEPTED && report.getType() != PlayerReport.ReportType.INAPPROPRIATE_NAME && report.getReason() != report.getReasonAccepted()) {
                    lore = String.format("%s;&r&fReason Accepted: **%s**", lore, report.getReasonAccepted().getName());
                }

                GUIItem guiItem = new GUIItem(guiitem, name, 1, lore);
                ItemStack itemStack = guiItem.getItem();
                ItemMeta meta = itemStack.getItemMeta();
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                itemStack.setItemMeta(meta);
                this.updateItem(row, column, new GUIItem(itemStack));

                column++;
                if (column == 8) {
                    row++;
                    column = 1;
                    if (row == 5) {
                        break;
                    }
                }

            }
            return;
        } else {
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
            return;
        }
    }
}
