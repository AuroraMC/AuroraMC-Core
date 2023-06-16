/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.gui.report;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.player.PlayerReport;
import net.auroramc.api.punishments.Rule;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import net.auroramc.smp.api.utils.gui.GUI;
import net.auroramc.smp.api.utils.gui.GUIItem;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class CloseReport extends GUI {

    private final AuroraMCServerPlayer player;

    public CloseReport(AuroraMCServerPlayer player) {
        super("&3&lClose Report", 2, true);

        this.player = player;

        fill("&r&f ", "");

        this.setItem(0, 4, new GUIItem(Material.PLAYER_HEAD, String.format("&3&l%s Report", WordUtils.capitalizeFully(player.getActiveReport().getType().name())), 1, String.format(";&r&fUser reported: **%s**;&r&fReason: **%s**", player.getActiveReport().getSuspectName(), player.getActiveReport().getReason().getName()), (short)0, false, player.getActiveReport().getSuspectName()));

        int column = 2;
        if (player.getActiveReport().getReason().getAltRule() != null) {
            column--;
            this.setItem(1, 7, new GUIItem(Material.GREEN_CONCRETE, String.format("&a&lAccept as %s", AuroraMCAPI.getRules().getRule(player.getActiveReport().getReason().getAltRule()).getRuleName()), 1, ";&r&fClick here to &aaccept&r&f this report!"));
        }

        Rule rule = AuroraMCAPI.getRules().getRule(player.getActiveReport().getReason().getDefaultRule());

        this.setItem(1, column, new GUIItem(Material.RED_CONCRETE, "&c&lReject", 1, ";&r&fClick here to &creject&r&f this report!"));
        this.setItem(1, column + 2, new GUIItem(Material.ORANGE_CONCRETE, "&6&lAccept under Different Rule", 1, ";&r&fClick here to &aaccept&r&f this report;&r&fbut change the rule type."));
        this.setItem(1, column + 4, new GUIItem(Material.GREEN_CONCRETE, String.format("&a&lAccept as %s", rule.getRuleName()), 1, ";&r&fClick here to &aaccept&r&f this report!"));

        this.setItem(2, 3, new GUIItem(Material.PLAYER_HEAD, "&c&lForward to Leadership", 1, ";&r&fForward this report to the;&r&fLeadership team for handling.", (short)0, false, "MHF_ArrowUp"));
        this.setItem(2, 5, new GUIItem(Material.BARRIER, "&c&lAbort", 1, ";&r&fAbort this report for another;&r&fstaff member to handle."));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (item.getType() == Material.GRAY_STAINED_GLASS_PANE) {
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 100, 0);
            return;
        }

        if (item.getType() == Material.PLAYER_HEAD && ChatColor.stripColor(item.getItemMeta().getDisplayName()).equals("Forward to Leadership")) {
            player.getActiveReport().forwardToLeadership(player);
            player.setActiveReport(null);
            player.closeInventory();
            return;
        } else if (item.getType() == Material.PLAYER_HEAD) {
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 100, 0);
            return;
        }

        if (item.getType() == Material.BARRIER) {
            player.getActiveReport().abort(player);
            player.setActiveReport(null);
            player.closeInventory();
            return;
        }

        switch (column) {
            case 1:
            case 2: {
                PlayerReport report = player.getActiveReport();
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        report.handle(player, PlayerReport.ReportOutcome.DENIED, null, false);
                    }
                }.runTaskAsynchronously(ServerAPI.getCore());
                player.setActiveReport(null);
                player.closeInventory();
                break;
            }
            case 3:
            case 4: {
                ChangeReportReasonListing listing = new ChangeReportReasonListing(player, player.getActiveReport().getType());
                listing.open(player);
                break;
            }
            case 5:
            case 6: {
                PlayerReport report = player.getActiveReport();
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        report.handle(player, PlayerReport.ReportOutcome.ACCEPTED, report.getReason(), false);
                    }
                }.runTaskAsynchronously(ServerAPI.getCore());
                player.setActiveReport(null);
                player.closeInventory();
                break;
            }
            case 7: {
                PlayerReport report = player.getActiveReport();
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        report.handle(player, PlayerReport.ReportOutcome.ACCEPTED, report.getReason(), true);
                    }
                }.runTaskAsynchronously(ServerAPI.getCore());
                player.setActiveReport(null);
                player.closeInventory();
                break;
            }
        }
    }
}
