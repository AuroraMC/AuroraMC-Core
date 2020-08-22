package network.auroramc.core.gui.stats.stats;

import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.players.AuroraMCPlayer;
import network.auroramc.core.api.stats.PlayerStatistics;
import network.auroramc.core.api.utils.gui.GUI;
import network.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class Stats extends GUI {

    private final AuroraMCPlayer player;
    private final String name;
    private final PlayerStatistics stats;

    public Stats(AuroraMCPlayer player, String targetName, PlayerStatistics targetStatistics) {
        super("&3&l" + targetName + "'s Statistics", 5, true);

        this.player = player;
        this.name = targetName;
        this.stats = targetStatistics;

        for (int i = 0; i <= 8; i++) {
            if (i < 6) {
                this.setItem(i, 0, new GUIItem(Material.STAINED_GLASS_PANE, String.format("&3&l%s's Statistics", player.getPlayer().getName()), 1, "", (short) 7));
                this.setItem(i, 8, new GUIItem(Material.STAINED_GLASS_PANE, String.format("&3&l%s's Statistics", player.getPlayer().getName()), 1, "", (short) 7));
            }
            this.setItem(0, i, new GUIItem(Material.STAINED_GLASS_PANE, String.format("&3&l%s's Statistics", player.getPlayer().getName()), 1, "", (short) 7));
            this.setItem(5, i, new GUIItem(Material.STAINED_GLASS_PANE, String.format("&3&l%s's Statistics", player.getPlayer().getName()), 1, "", (short) 7));
        }
        this.setItem(0, 4, new GUIItem(Material.SKULL_ITEM, String.format("&3&l%s's Statistics", player.getPlayer().getName()), 1, "", (short) 3, false, player.getPlayer().getName()));
        this.setItem(1, 4, new GUIItem(Material.NETHER_STAR, "&a&lGeneral Statistics", 1, "&rClick here to view your &aGeneral Statistics"));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (row == 1 && column == 4) {
            AuroraMCAPI.closeGUI(player);
            GeneralStatistics stats = new GeneralStatistics(player, name, this.stats);
            stats.open(player);
            AuroraMCAPI.openGUI(player, stats);
        } else {
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
        }
    }
}
