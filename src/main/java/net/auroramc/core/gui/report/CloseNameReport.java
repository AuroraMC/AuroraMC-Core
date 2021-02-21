package net.auroramc.core.gui.report;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.PlayerReport;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class CloseNameReport extends GUI {

    private final AuroraMCPlayer player;

    public CloseNameReport(AuroraMCPlayer player) {
        super("&3&lClose Name Report", 2, true);

        this.player = player;

        fill("&r ", "");

        this.setItem(0, 4, new GUIItem(Material.SKULL_ITEM, "&3&lUsername Report", 1, String.format(";&rReported Username: **%s**.", player.getActiveReport().getSuspectName()), (short)3, false, player.getActiveReport().getSuspectName()));

        this.setItem(1, 2, new GUIItem(Material.STAINED_CLAY, "&c&lInappropriate", 1, ";&rClick here to mark this;&rusername as &cInappropriate&r.", (short)14));
        this.setItem(1, 6, new GUIItem(Material.STAINED_CLAY, "&a&lAppropriate", 1, ";&rClick here to mark this;&rusername as &aAppropriate&r.", (short)13));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (item.getType() == Material.STAINED_GLASS_PANE || item.getType() == Material.SKULL_ITEM) {
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
            return;
        }

        if (item.getDurability() == 14) {
            PlayerReport report = player.getActiveReport();
            new BukkitRunnable(){
                @Override
                public void run() {
                    report.handle(player, PlayerReport.ReportOutcome.ACCEPTED, null, false);
                }
            }.runTaskAsynchronously(AuroraMCAPI.getCore());
        } else {
            PlayerReport report = player.getActiveReport();

            new BukkitRunnable(){
                @Override
                public void run() {
                    report.handle(player, PlayerReport.ReportOutcome.DENIED, null, false);
                }
            }.runTaskAsynchronously(AuroraMCAPI.getCore());
        }
        player.setActiveReport(null);
        player.getPlayer().closeInventory();
    }

}
