package net.auroramc.core.gui.preferences;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class GamePreferences extends GUI {

    private AuroraMCPlayer player;

    public GamePreferences(AuroraMCPlayer player) {
        super("&3&lGame Preferences", 5, true);
        this.player = player;

        for (int i = 0; i <= 8; i++) {
            if (i < 6) {
                this.setItem(i, 0, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lGame Preferences", 1, "", (short) 7));
                this.setItem(i, 8, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lGame Preferences", 1, "", (short) 7));
            }
            this.setItem(0, i, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lGame Preferences", 1, "", (short) 7));
            this.setItem(5, i, new GUIItem(Material.STAINED_GLASS_PANE, "&3&lGame Preferences", 1, "", (short) 7));
        }
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
                AuroraMCAPI.openGUI(player, prefs);
                break;
            default:
                player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                break;
        }
    }

}
