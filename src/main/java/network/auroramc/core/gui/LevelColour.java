package network.auroramc.core.gui;

import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.players.AuroraMCPlayer;
import network.auroramc.core.api.utils.gui.GUI;
import network.auroramc.core.api.utils.gui.GUIItem;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class LevelColour extends GUI {

    private AuroraMCPlayer player;

    public LevelColour(AuroraMCPlayer player) {
        super("&3&lSelect your Level colour!", 5, true);

        this.player = player;

        for (int i = 0; i <= 8; i++) {
            if (i < 6) {
                this.setItem(i, 0, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lYour Level Colour", 1, "", (short) 7));
                this.setItem(i, 8, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lYour Level Colour", 1, "", (short) 7));
            }
            this.setItem(0, i, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lYour Level Colour", 1, "", (short) 7));
            this.setItem(5, i, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lYour Level Colour", 1, "", (short) 7));
        }
        this.setItem(0, 4, new GUIItem(Material.SKULL_ITEM, "&3&lYour Level Colour", 1, String.format("&rCurrent Level Colour: &%s%s", player.getActiveSubscription().getLevelColor(), WordUtils.capitalizeFully(ChatColor.getByChar(player.getActiveSubscription().getLevelColor()).name().replace("_", " ").toLowerCase())), (short) 3, false));


        this.setItem(1, 3, new GUIItem(Material.LEATHER_CHESTPLATE, "&0&lBlack", 1, "&rSet &0Black&r as your Level colour", (short) 0, (player.getActiveSubscription().getLevelColor() == '0'), Color.BLACK));
        this.setItem(1, 4, new GUIItem(Material.LEATHER_CHESTPLATE, "&1&lDark Blue", 1, "&rSet &1Dark Blue&r as your Level colour", (short) 0, (player.getActiveSubscription().getLevelColor() == '1'), Color.fromRGB(0, 0, 170)));
        this.setItem(1, 5, new GUIItem(Material.LEATHER_CHESTPLATE, "&2&lDark Green", 1, "&rSet &2Dark Green&r as your Level colour", (short) 0, (player.getActiveSubscription().getLevelColor() == '2'), Color.fromRGB(0, 170, 0)));

        this.setItem(2, 2, new GUIItem(Material.LEATHER_CHESTPLATE, "&3&lDark Aqua", 1, "&rSet &3Dark Aqua&r as your Level colour", (short) 0, (player.getActiveSubscription().getLevelColor() == '3'), Color.fromRGB(0, 170, 170)));
        this.setItem(2, 3, new GUIItem(Material.LEATHER_CHESTPLATE, "&4&lDark Red", 1, "&rSet &4Dark Red&r as your Level colour", (short) 0, (player.getActiveSubscription().getLevelColor() == '4'), Color.fromRGB(170, 0, 0)));
        this.setItem(2, 4, new GUIItem(Material.LEATHER_CHESTPLATE, "&5&lDark Purple", 1, "&rSet &5Dark Purple&r as your Level colour", (short) 0, (player.getActiveSubscription().getLevelColor() == '5'), Color.fromRGB(170, 0, 170)));
        this.setItem(2, 5, new GUIItem(Material.LEATHER_CHESTPLATE, "&6&lGold", 1, "&rSet &6Gold&r as your Level colour", (short) 0, (player.getActiveSubscription().getLevelColor() == '6'), Color.fromRGB(255, 170, 0)));
        this.setItem(2, 6, new GUIItem(Material.LEATHER_CHESTPLATE, "&7&lGray", 1, "&rSet &7Gray&r as your Level colour", (short) 0, (player.getActiveSubscription().getLevelColor() == '7'), Color.fromRGB(170, 170, 170)));

        this.setItem(3, 2, new GUIItem(Material.LEATHER_CHESTPLATE, "&8&lDark Gray", 1, "&rSet &8Dark Gray&r as your Level colour", (short) 0, (player.getActiveSubscription().getLevelColor() == '8'), Color.fromRGB(85, 85, 85)));
        this.setItem(3, 3, new GUIItem(Material.LEATHER_CHESTPLATE, "&9&lBlue", 1, "&rSet &9Blue&r as your Level colour", (short) 0, (player.getActiveSubscription().getLevelColor() == '9'), Color.fromRGB(85, 85, 255)));
        this.setItem(3, 4, new GUIItem(Material.LEATHER_CHESTPLATE, "&a&lGreen", 1, "&rSet &aGreen&r as your Level colour", (short) 0, (player.getActiveSubscription().getLevelColor() == 'a'), Color.fromRGB(85, 255, 85)));
        this.setItem(3, 5, new GUIItem(Material.LEATHER_CHESTPLATE, "&b&lAqua", 1, "&rSet &bAqua&r as your Level colour", (short) 0, (player.getActiveSubscription().getLevelColor() == 'b'), Color.fromRGB(85, 255, 255)));
        this.setItem(3, 6, new GUIItem(Material.LEATHER_CHESTPLATE, "&c&lRed", 1, "&rSet &cRed&r as your Level colour", (short) 0, (player.getActiveSubscription().getLevelColor() == 'c'), Color.fromRGB(255, 85, 85)));

        this.setItem(4, 3, new GUIItem(Material.LEATHER_CHESTPLATE, "&d&lLight Purple", 1, "&rSet &dLight Purple&r as your Level colour", (short) 0, (player.getActiveSubscription().getLevelColor() == 'd'), Color.fromRGB(255, 85, 255)));
        this.setItem(4, 4, new GUIItem(Material.LEATHER_CHESTPLATE, "&e&lYellow", 1, "&rSet &eYellow&r as your Level colour", (short) 0, (player.getActiveSubscription().getLevelColor() == 'e'), Color.fromRGB(255, 255, 85)));
        this.setItem(4, 5, new GUIItem(Material.LEATHER_CHESTPLATE, "&f&lWhite", 1, "&rSet &fWhite&r as your Level colour", (short) 0, (player.getActiveSubscription().getLevelColor() == 'f'), Color.fromRGB(255, 255, 255)));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (item.getType() == Material.LEATHER_CHESTPLATE) {
            ChatColor color = ChatColor.valueOf(ChatColor.stripColor(item.getItemMeta().getDisplayName()).replace(" ", "_").toUpperCase());
            if (player.getActiveSubscription() != null) {
                player.getActiveSubscription().setLevelColor(color.getChar());
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Plus", String.format("Your Level colour was set to **%s**.", ChatColor.stripColor(item.getItemMeta().getDisplayName()))));
            } else {
               player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Plus", "Your Plus subscription has expired!"));
            }
            player.getPlayer().closeInventory();
        } else {
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
        }
    }
}
