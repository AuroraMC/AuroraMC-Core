package net.auroramc.core.gui.preferences;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Preferences extends GUI {

    private AuroraMCPlayer player;

    public Preferences(AuroraMCPlayer player) {
        super("&3&lYour Preferences", 5, true);
        this.player = player;
        border("&3&lYour Preferences", "");

        this.setItem(2, 2, new GUIItem(Material.PAPER, "&d&lSocial Preferences"));
        this.setItem(2, 4, new GUIItem(Material.MINECART, "&e&lGame Preferences"));
        this.setItem(2, 6, new GUIItem(Material.BOOK_AND_QUILL, "&b&lChat Preferences"));

        if (player.hasPermission("social") || player.hasPermission("moderation")) {
            if (player.hasPermission("admin")) {
                this.setItem(3, 3, new GUIItem(Material.NETHER_STAR, "&c&lMiscellaneous Preferences"));
                GUIItem guiItem = new GUIItem(Material.DIAMOND_AXE, "&9&lStaff Preferences");
                ItemStack itemStack = guiItem.getItem();
                ItemMeta meta = itemStack.getItemMeta();
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                itemStack.setItemMeta(meta);
                this.setItem(3, 1, new GUIItem(itemStack));
                this.setItem(3, 5, new GUIItem(Material.DIAMOND, "&6&lMedia Preferences"));
                this.setItem(3, 7, new GUIItem(Material.GLOWSTONE_DUST, "&6&lStaff Management Preferences"));
            } else {
                if (player.hasPermission("staffmanagement")) {
                    this.setItem(3, 2, new GUIItem(Material.NETHER_STAR, "&c&lMiscellaneous Preferences"));
                    this.setItem(3, 6, new GUIItem(Material.GLOWSTONE_DUST, "&6&lStaff Management Preferences"));
                    GUIItem guiItem = new GUIItem(Material.DIAMOND_AXE, "&9&lStaff Preferences");
                    ItemStack itemStack = guiItem.getItem();
                    ItemMeta meta = itemStack.getItemMeta();
                    meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    itemStack.setItemMeta(meta);
                    this.setItem(3, 4, new GUIItem(itemStack));
                } else {
                    this.setItem(3, 3, new GUIItem(Material.NETHER_STAR, "&c&lMiscellaneous Preferences"));
                    if (player.hasPermission("moderation")) {
                        GUIItem guiItem = new GUIItem(Material.DIAMOND_AXE, "&9&lStaff Preferences");
                        ItemStack itemStack = guiItem.getItem();
                        ItemMeta meta = itemStack.getItemMeta();
                        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                        itemStack.setItemMeta(meta);
                        this.setItem(3, 5, new GUIItem(itemStack));
                    } else {
                        this.setItem(3, 5, new GUIItem(Material.DIAMOND, "&6&lMedia Preferences"));
                    }
                }
            }
        } else {
            this.setItem(3, 4, new GUIItem(Material.NETHER_STAR, "&c&lMiscellaneous Preferences"));
        }
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        switch (item.getType()) {
            case PAPER:
                SocialPreferences socialPreferences = new SocialPreferences(player);
                socialPreferences.open(player);
                AuroraMCAPI.openGUI(player, socialPreferences);
                break;
            case MINECART:
                GamePreferences gamePreferences = new GamePreferences(player);
                gamePreferences.open(player);
                AuroraMCAPI.openGUI(player, gamePreferences);
                break;
            case BOOK_AND_QUILL:
                ChatPreferences chatPreferences = new ChatPreferences(player);
                chatPreferences.open(player);
                AuroraMCAPI.openGUI(player, chatPreferences);
                break;
            case NETHER_STAR:
                MiscPreferences miscPreferences = new MiscPreferences(player);
                miscPreferences.open(player);
                AuroraMCAPI.openGUI(player, miscPreferences);
                break;
            case DIAMOND:
                MediaPreferences mediaPreferences = new MediaPreferences(player);
                mediaPreferences.open(player);
                AuroraMCAPI.openGUI(player, mediaPreferences);
                break;
            case DIAMOND_AXE:
                StaffPreferences staffPreferences = new StaffPreferences(player);
                staffPreferences.open(player);
                AuroraMCAPI.openGUI(player, staffPreferences);
                break;
            case GLOWSTONE_DUST:
                StaffManagementPreferences staffManagementPreferences = new StaffManagementPreferences(player);
                staffManagementPreferences.open(player);
                AuroraMCAPI.openGUI(player, staffManagementPreferences);
                break;
        }
    }
}
