/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.gui.report;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.PlayerReport;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class ChangeReportReasonChooseRule extends GUI {

    private final AuroraMCPlayer player;
    private final PlayerReport.ReportReason reason;

    public ChangeReportReasonChooseRule(AuroraMCPlayer player, PlayerReport.ReportReason reason) {
        super("&3&lChoose a Rule", 2, true);

        this.player = player;
        this.reason = reason;

        fill("&r ", "");

        this.setItem(0, 4, new GUIItem(Material.SKULL_ITEM, String.format("&3&l%s Report", WordUtils.capitalizeFully(player.getActiveReport().getType().name())), 1, String.format(";&rUser reported: **%s**;&rReason: **%s**", player.getActiveReport().getSuspectName(), player.getActiveReport().getReason().getName()), (short)3, false, player.getActiveReport().getSuspectName()));

        this.setItem(1, 2, new GUIItem(Material.STAINED_CLAY, String.format("&a&lAccept as %s", AuroraMCAPI.getRules().getRule(reason.getDefaultRule()).getRuleName()), 1, ";&rClick here to &aaccept&r this report!" ,(short)13));
        this.setItem(1, 6, new GUIItem(Material.STAINED_CLAY, String.format("&a&lAccept as %s", AuroraMCAPI.getRules().getRule(reason.getAltRule()).getRuleName()), 1, ";&rClick here to &aaccept&r this report!" ,(short)13));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (item.getType() == Material.STAINED_GLASS_PANE || item.getType() == Material.SKULL_ITEM) {
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
            return;
        }
        PlayerReport report = player.getActiveReport();
        new BukkitRunnable(){
            @Override
            public void run() {
                report.handle(player, PlayerReport.ReportOutcome.ACCEPTED, reason, column != 2);
            }
        }.runTaskAsynchronously(AuroraMCAPI.getCore());
        player.setActiveReport(null);
        player.getPlayer().closeInventory();
    }
}
