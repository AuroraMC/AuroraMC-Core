/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.gui.report;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.player.PlayerReport;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import net.auroramc.smp.api.utils.gui.GUI;
import net.auroramc.smp.api.utils.gui.GUIItem;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class ChangeReportReasonChooseRule extends GUI {

    private final AuroraMCServerPlayer player;
    private final PlayerReport.ReportReason reason;

    public ChangeReportReasonChooseRule(AuroraMCServerPlayer player, PlayerReport.ReportReason reason) {
        super("&3&lChoose a Rule", 2, true);

        this.player = player;
        this.reason = reason;

        fill("&r&f ", "");

        this.setItem(0, 4, new GUIItem(Material.PLAYER_HEAD, String.format("&3&l%s Report", WordUtils.capitalizeFully(player.getActiveReport().getType().name())), 1, String.format(";&r&fUser reported: **%s**;&r&fReason: **%s**", player.getActiveReport().getSuspectName(), player.getActiveReport().getReason().getName()), (short)0, false, player.getActiveReport().getSuspectName()));

        this.setItem(1, 2, new GUIItem(Material.GREEN_CONCRETE, String.format("&a&lAccept as %s", AuroraMCAPI.getRules().getRule(reason.getDefaultRule()).getRuleName()), 1, ";&r&fClick here to &aaccept&r&f this report!"));
        this.setItem(1, 6, new GUIItem(Material.GREEN_CONCRETE, String.format("&a&lAccept as %s", AuroraMCAPI.getRules().getRule(reason.getAltRule()).getRuleName()), 1, ";&r&fClick here to &aaccept&r&f this report!"));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (item.getType() == Material.GRAY_STAINED_GLASS_PANE || item.getType() == Material.PLAYER_HEAD) {
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 100, 0);
            return;
        }
        PlayerReport report = player.getActiveReport();
        new BukkitRunnable(){
            @Override
            public void run() {
                report.handle(player, PlayerReport.ReportOutcome.ACCEPTED, reason, column != 2);
            }
        }.runTaskAsynchronously(ServerAPI.getCore());
        player.setActiveReport(null);
        player.closeInventory();
    }
}
