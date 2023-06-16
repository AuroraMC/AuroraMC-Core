/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.gui.plus;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import net.auroramc.smp.api.utils.gui.GUI;
import net.auroramc.smp.api.utils.gui.GUIItem;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class LevelColour extends GUI {

    private AuroraMCServerPlayer player;

    public LevelColour(AuroraMCServerPlayer player) {
        super("&3&lSelect your Level colour!", 5, true);

        this.player = player;

        border("&3&lYour Level Colour", "");
        this.setItem(0, 4, new GUIItem(Material.PLAYER_HEAD, "&3&lYour Level Colour", 1, String.format("&r&fCurrent Level Colour: %s%s", player.getActiveSubscription().getLevelColor(), WordUtils.capitalizeFully(player.getActiveSubscription().getLevelColor().name().replace("_", " ").toLowerCase())), (short) 0, false));


        this.setItem(1, 3, new GUIItem(Material.LEATHER_CHESTPLATE, "&0&lBlack", 1, "&r&fSet &0Black&r&f as your Level colour", (short) 0, (player.getActiveSubscription().getLevelColor() == ChatColor.BLACK), Color.BLACK));
        this.setItem(1, 4, new GUIItem(Material.LEATHER_CHESTPLATE, "&1&lDark Blue", 1, "&r&fSet &1Dark Blue&r&f as your Level colour", (short) 0, (player.getActiveSubscription().getLevelColor() == ChatColor.DARK_BLUE), Color.fromRGB(0, 0, 170)));
        this.setItem(1, 5, new GUIItem(Material.LEATHER_CHESTPLATE, "&2&lDark Green", 1, "&r&fSet &2Dark Green&r&f as your Level colour", (short) 0, (player.getActiveSubscription().getLevelColor() == ChatColor.DARK_GREEN), Color.fromRGB(0, 170, 0)));

        this.setItem(2, 2, new GUIItem(Material.LEATHER_CHESTPLATE, "&3&lDark Aqua", 1, "&r&fSet &3Dark Aqua&r&f as your Level colour", (short) 0, (player.getActiveSubscription().getLevelColor() == ChatColor.DARK_AQUA), Color.fromRGB(0, 170, 170)));
        this.setItem(2, 3, new GUIItem(Material.LEATHER_CHESTPLATE, "&4&lDark Red", 1, "&r&fSet &4Dark Red&r&f as your Level colour", (short) 0, (player.getActiveSubscription().getLevelColor() == ChatColor.DARK_RED), Color.fromRGB(170, 0, 0)));
        this.setItem(2, 4, new GUIItem(Material.LEATHER_CHESTPLATE, "&5&lDark Purple", 1, "&r&fSet &5Dark Purple&r&f as your Level colour", (short) 0, (player.getActiveSubscription().getLevelColor() == ChatColor.DARK_PURPLE), Color.fromRGB(170, 0, 170)));
        this.setItem(2, 5, new GUIItem(Material.LEATHER_CHESTPLATE, "&6&lGold", 1, "&r&fSet &6Gold&r&f as your Level colour", (short) 0, (player.getActiveSubscription().getLevelColor() == ChatColor.GOLD), Color.fromRGB(255, 170, 0)));
        this.setItem(2, 6, new GUIItem(Material.LEATHER_CHESTPLATE, "&7&lGray", 1, "&r&fSet &7Gray&r&f as your Level colour", (short) 0, (player.getActiveSubscription().getLevelColor() == ChatColor.GRAY), Color.fromRGB(170, 170, 170)));

        this.setItem(3, 2, new GUIItem(Material.LEATHER_CHESTPLATE, "&8&lDark Gray", 1, "&r&fSet &8Dark Gray&r&f as your Level colour", (short) 0, (player.getActiveSubscription().getLevelColor() == ChatColor.DARK_GRAY), Color.fromRGB(85, 85, 85)));
        this.setItem(3, 3, new GUIItem(Material.LEATHER_CHESTPLATE, "&9&lBlue", 1, "&r&fSet &9Blue&r&f as your Level colour", (short) 0, (player.getActiveSubscription().getLevelColor() == ChatColor.BLUE), Color.fromRGB(85, 85, 255)));
        this.setItem(3, 4, new GUIItem(Material.LEATHER_CHESTPLATE, "&a&lGreen", 1, "&r&fSet &aGreen&r&f as your Level colour", (short) 0, (player.getActiveSubscription().getLevelColor() == ChatColor.GREEN), Color.fromRGB(85, 255, 85)));
        this.setItem(3, 5, new GUIItem(Material.LEATHER_CHESTPLATE, "&b&lAqua", 1, "&r&fSet &bAqua&r&f as your Level colour", (short) 0, (player.getActiveSubscription().getLevelColor() == ChatColor.AQUA), Color.fromRGB(85, 255, 255)));
        this.setItem(3, 6, new GUIItem(Material.LEATHER_CHESTPLATE, "&c&lRed", 1, "&r&fSet &cRed&r&f as your Level colour", (short) 0, (player.getActiveSubscription().getLevelColor() == ChatColor.RED), Color.fromRGB(255, 85, 85)));

        this.setItem(4, 3, new GUIItem(Material.LEATHER_CHESTPLATE, "&d&lLight Purple", 1, "&r&fSet &dLight Purple&r&f as your Level colour", (short) 0, (player.getActiveSubscription().getLevelColor() == ChatColor.LIGHT_PURPLE), Color.fromRGB(255, 85, 255)));
        this.setItem(4, 4, new GUIItem(Material.LEATHER_CHESTPLATE, "&e&lYellow", 1, "&r&fSet &eYellow&r&f as your Level colour", (short) 0, (player.getActiveSubscription().getLevelColor() == ChatColor.YELLOW), Color.fromRGB(255, 255, 85)));
        this.setItem(4, 5, new GUIItem(Material.LEATHER_CHESTPLATE, "&f&lWhite", 1, "&r&fSet &fWhite&r&f as your Level colour", (short) 0, (player.getActiveSubscription().getLevelColor() == ChatColor.WHITE), Color.fromRGB(255, 255, 255)));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (item.getType() == Material.LEATHER_CHESTPLATE) {
            ChatColor color = ChatColor.valueOf(ChatColor.stripColor(item.getItemMeta().getDisplayName()).replace(" ", "_").toUpperCase());
            if (player.getActiveSubscription() != null) {
                player.getActiveSubscription().setLevelColor(color, true);
                player.sendMessage(TextFormatter.pluginMessage("Plus", String.format("Your Level colour was set to **%s**.", ChatColor.stripColor(item.getItemMeta().getDisplayName()))));
            } else {
               player.sendMessage(TextFormatter.pluginMessage("Plus", "Your Plus subscription has expired!"));
            }
            player.closeInventory();
        } else {
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 100, 0);
        }
    }
}
