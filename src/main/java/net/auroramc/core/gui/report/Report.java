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


public class Report extends GUI {

    private final AuroraMCPlayer player;
    private final int id;
    private final String name;

    public Report(AuroraMCPlayer player, int id, String name) {
        super(String.format("&3&lReport %s", name), 2, true);

        this.player = player;
        this.id = id;
        this.name = name;

        border(String.format("&3&lReport %s", name), "");

        this.setItem(0, 4, new GUIItem(Material.SKULL_ITEM, String.format("&3&lReport %s", name), 1, "&rPlease choose a type of report.", (short)3, false, name));

        this.setItem(1, 1, new GUIItem(Material.BOOK_AND_QUILL, "&3&lChat Report", 1, ";&rClick here to view a list;&rof possible Chat offences."));
        this.setItem(1, 3, new GUIItem(Material.SIGN, "&3&lMisc Report", 1, ";&rClick here to view a list;&rof possible Misc offences."));
        this.setItem(1, 5, new GUIItem(Material.IRON_SWORD, "&3&lHacking Report", 1, ";&rClick here to view a list;&rof possible Hacking offences."));
        this.setItem(1, 7, new GUIItem(Material.NAME_TAG, "&3&lInappropriate Name Report", 1, ";&rClick here to report this username;&rfor reviewal by our Leadership team."));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (item.getType() == Material.GLASS || item.getType() == Material.SKULL_ITEM) {
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
            return;
        }

        PlayerReport.ReportType type = PlayerReport.ReportType.MISC;
        switch (column) {
            case 1:
                ChatType chatType = new ChatType(player, id, name);
                chatType.open(player);
                AuroraMCAPI.openGUI(player, chatType);
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
                }.runTaskAsynchronously(AuroraMCAPI.getCore());
                player.getPlayer().closeInventory();
                return;
        }

        ReportReasonListing listing = new ReportReasonListing(player, id, name, type);
        listing.open(player);
        AuroraMCAPI.openGUI(player, listing);
    }
}
