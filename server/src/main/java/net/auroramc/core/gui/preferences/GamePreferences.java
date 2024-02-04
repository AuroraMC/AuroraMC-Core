/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.core.gui.preferences;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class GamePreferences extends GUI {

    private AuroraMCServerPlayer player;

    public GamePreferences(AuroraMCServerPlayer player) {
        super("&3&lGame Preferences", 5, true);
        this.player = player;

        border("&3&lGame Preferences", "");
        this.setItem(0, 0, new GUIItem(Material.ARROW, "&3&lBack"));

    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        switch (item.getType()) {
            case INK_SACK:

                break;
            case ARROW:
                Preferences prefs = new Preferences(player);
                prefs.open(player);
                break;
            default:
                player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
                break;
        }
    }

}
