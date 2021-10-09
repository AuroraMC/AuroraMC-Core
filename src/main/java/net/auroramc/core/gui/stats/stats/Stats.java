/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.gui.stats.stats;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.PlusSubscription;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.stats.PlayerStatistics;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class Stats extends GUI {

    private final AuroraMCPlayer player;
    private final String name;
    private final PlayerStatistics stats;
    private final PlusSubscription subscription;

    public Stats(AuroraMCPlayer player, String targetName, PlayerStatistics targetStatistics, PlusSubscription subscription) {
        super("&3&l" + targetName + "'s Statistics", 5, true);

        this.player = player;
        this.name = targetName;
        this.stats = targetStatistics;
        this.subscription = subscription;

        border(String.format("&3&l%s's Statistics", name), "");
        this.setItem(0, 4, new GUIItem(Material.SKULL_ITEM, String.format("&3&l%s's Statistics", name), 1, "", (short) 3, false, name));
        this.setItem(1, 4, new GUIItem(Material.NETHER_STAR, "&a&lGeneral Statistics", 1, "&rClick here to view &aGeneral Statistics"));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (row == 1 && column == 4) {
            AuroraMCAPI.closeGUI(player);
            GeneralStatistics stats = new GeneralStatistics(player, name, this.stats, this.subscription);
            stats.open(player);
            AuroraMCAPI.openGUI(player, stats);
        } else {
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
        }
    }
}
