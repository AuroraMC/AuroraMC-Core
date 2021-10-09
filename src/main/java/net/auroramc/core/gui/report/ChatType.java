/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.gui.report;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.PlayerReport;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.core.managers.ReportManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class ChatType extends GUI {

    private final AuroraMCPlayer player;
    private final int id;
    private final String name;
    private PlayerReport.ReportReason reason;

    public ChatType(AuroraMCPlayer player, int id, String name) {
        super("&3&lPlease choose a chat type", 2, true);

        this.player = player;
        this.id = id;
        this.name = name;
        this.reason = null;

        border(String.format("&3&lReport %s", name), "");

        this.setItem(0, 4, new GUIItem(Material.BOOK_AND_QUILL, String.format("&3&lReport %s", name), 1, "&rPlease choose a type of report."));

        this.setItem(1, 2, new GUIItem(Material.PAPER, "&3Public Chat", 1, ";&rClick here to report a chat offence;&rthat happened in &bPublic Chat&r."));
        this.setItem(1, 4, new GUIItem(Material.EMPTY_MAP, "&3Private Messages", 1, ";&rClick here to report a chat offence;&rthat happened in &bPrivate Messages&r."));
        this.setItem(1, 6, new GUIItem(Material.FIREWORK, "&3Party Chat", 1, ";&rClick here to report a chat offence;&rthat happened in &bParty Chat&r."));
    }

    public ChatType(AuroraMCPlayer player, int id, String name, PlayerReport.ReportReason reason) {
        this(player, id, name);
        this.reason = reason;
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (item.getType() == Material.GLASS || item.getType() == Material.SKULL_ITEM) {
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
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
            }.runTaskAsynchronously(AuroraMCAPI.getCore());
            player.getPlayer().closeInventory();
            return;
        }

        ChatReportReasonListing chatReportReasonListing = new ChatReportReasonListing(player, id, name, type);
        chatReportReasonListing.open(player);
        AuroraMCAPI.openGUI(player, chatReportReasonListing);
    }
}
