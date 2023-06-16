/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.gui.report;

import net.auroramc.api.player.PlayerReport;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import net.auroramc.smp.api.utils.gui.GUI;
import net.auroramc.smp.api.utils.gui.GUIItem;
import net.auroramc.smp.managers.ReportManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class ChatType extends GUI {

    private final AuroraMCServerPlayer player;
    private final int id;
    private final String name;
    private PlayerReport.ReportReason reason;

    public ChatType(AuroraMCServerPlayer player, int id, String name) {
        super("&3&lPlease choose a chat type", 2, true);

        this.player = player;
        this.id = id;
        this.name = name;
        this.reason = null;

        border(String.format("&3&lReport %s", name), "");

        this.setItem(0, 4, new GUIItem(Material.WRITABLE_BOOK, String.format("&3&lReport %s", name), 1, "&r&fPlease choose a type of report."));

        this.setItem(1, 2, new GUIItem(Material.PAPER, "&3Public Chat", 1, ";&r&fClick here to report a chat offence;&r&fthat happened in &bPublic Chat&r&f."));
        this.setItem(1, 4, new GUIItem(Material.MAP, "&3Private Messages", 1, ";&r&fClick here to report a chat offence;&r&fthat happened in &bPrivate Messages&r&f."));
        this.setItem(1, 6, new GUIItem(Material.FIREWORK_ROCKET, "&3Party Chat", 1, ";&r&fClick here to report a chat offence;&r&fthat happened in &bParty Chat&r&f."));
    }

    public ChatType(AuroraMCServerPlayer player, int id, String name, PlayerReport.ReportReason reason) {
        this(player, id, name);
        this.reason = reason;
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (item.getType() == Material.GRAY_STAINED_GLASS_PANE || item.getType() == Material.PLAYER_HEAD) {
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 100, 0);
            return;
        }

        PlayerReport.ChatType type = PlayerReport.ChatType.PUBLIC;
        switch (column) {
            case 4:
                type = PlayerReport.ChatType.PRIVATE;
                break;
            case 6:
                type = PlayerReport.ChatType.PARTY;
                break;
        }

        final PlayerReport.ChatType chatType = type;

        if (reason != null) {
            new BukkitRunnable(){
                @Override
                public void run() {
                    ReportManager.newReport(id, name, player, System.currentTimeMillis(), PlayerReport.ReportType.CHAT, chatType, reason, PlayerReport.QueueType.NORMAL);
                }
            }.runTaskAsynchronously(ServerAPI.getCore());
            player.closeInventory();
            return;
        }

        ChatReportReasonListing chatReportReasonListing = new ChatReportReasonListing(player, id, name, type);
        chatReportReasonListing.open(player);
    }
}
