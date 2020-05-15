package network.auroramc.core.gui.punish;

import network.auroramc.core.AuroraMC;
import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.players.AuroraMCPlayer;
import network.auroramc.core.api.utils.gui.GUI;
import network.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Punish extends GUI {

    private final AuroraMCPlayer player;
    private final String name;
    private final int id;
    private final String extraDetails;



    public Punish(AuroraMCPlayer player, String name, int id, String extraDetails) {
        super(String.format("&3&lPunish %s", name), 2, true);

        this.extraDetails = extraDetails;
        this.name = name;
        this.id = id;
        this.player = player;

        this.setItem(0, 4, new GUIItem(Material.SKULL_ITEM, String.format("&3&lPunish %s", name), 1, "&rPlease choose a punishment type.", (short)3, false, name));

        this.setItem(1, 2, new GUIItem(Material.BOOK_AND_QUILL, "&3&lChat Rules", 1, String.format("&rPunish %s for a chat rule", name)));
        this.setItem(1, 4, new GUIItem(Material.IRON_SWORD, "&3&lGame Rules", 1, String.format("&rPunish %s for a game rule", name)));
        this.setItem(1, 6, new GUIItem(Material.SIGN, "&3&lMisc Rules", 1, String.format("&rPunish %s for a miscellaneous rule", name)));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        int type;
        switch (item.getType()) {
            case BOOK_AND_QUILL:
                type = 1;
                break;
            case IRON_SWORD:
                type = 2;
                break;
            case SIGN:
                type = 3;
                break;
            default:
                type = -1;
        }

        if (type != -1) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    RuleListing ruleListing = new RuleListing(player, name, id, type, extraDetails, AuroraMCAPI.getDbManager().getPunishmentHistory(id));
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            AuroraMCAPI.closeGUI(player);
                            ruleListing.open(player);
                            AuroraMCAPI.openGUI(player, ruleListing);
                        }
                    }.runTask(AuroraMCAPI.getCore());
                }
            }.runTaskAsynchronously(AuroraMCAPI.getCore());
        } else {
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
        }
    }
}
