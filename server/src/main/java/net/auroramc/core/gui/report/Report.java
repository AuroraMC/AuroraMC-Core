/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
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
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;


public class Report extends GUI {

    private final AuroraMCServerPlayer player;
    private final int id;
    private final String name;

    public Report(AuroraMCServerPlayer player, int id, String name) {
        super(String.format("&3&lReport %s", name), 2, true);

        this.player = player;
        this.id = id;
        this.name = name;

        border(String.format("&3&lReport %s", name), "");

        this.setItem(0, 4, new GUIItem(Material.SKULL_ITEM, String.format("&3&lReport %s", name), 1, "&r&fPlease choose a type of report.", (short)3, false, name));

        this.setItem(1, 1, new GUIItem(Material.BOOK_AND_QUILL, "&3&lChat Report", 1, ";&r&fClick here to view a list;&r&fof possible Chat offences."));
        this.setItem(1, 3, new GUIItem(Material.SIGN, "&3&lMisc Report", 1, ";&r&fClick here to view a list;&r&fof possible Misc offences."));
        GUIItem guiItem = new GUIItem(Material.IRON_SWORD, "&3&lHacking Report", 1, ";&r&fClick here to view a list;&r&fof possible Hacking offences.");
        ItemStack itemStack = guiItem.getItemStack();
        ItemMeta meta = itemStack.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(meta);
        this.setItem(1, 5, new GUIItem(itemStack));
        this.setItem(1, 7, new GUIItem(Material.NAME_TAG, "&3&lInappropriate Name Report", 1, ";&r&fClick here to report this username;&r&ffor reviewal by our Leadership team."));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (item.getType() == Material.GLASS || item.getType() == Material.SKULL_ITEM) {
            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
            return;
        }

        PlayerReport.ReportType type = PlayerReport.ReportType.MISC;
        switch (column) {
            case 1:
                ChatType chatType = new ChatType(player, id, name);
                chatType.open(player);
                return;
            case 5:
                type = PlayerReport.ReportType.HACKING;
                break;
            case 7:
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        ReportManager.newReport(id, name, player, System.currentTimeMillis(), PlayerReport.ReportType.INAPPROPRIATE_NAME, null, PlayerReport.ReportReason.INAPPROPRIATE_NAME, PlayerReport.QueueType.LEADERSHIP);
                    }
                }.runTaskAsynchronously(ServerAPI.getCore());
                player.closeInventory();
                return;
        }

        ReportReasonListing listing = new ReportReasonListing(player, id, name, type);
        listing.open(player);
    }
}