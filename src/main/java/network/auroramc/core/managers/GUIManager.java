package network.auroramc.core.managers;

import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.players.AuroraMCPlayer;
import network.auroramc.core.api.utils.gui.GUI;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class GUIManager implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getPlayer() instanceof Player) {
            Player pl = (Player) e.getPlayer();
            AuroraMCPlayer player = AuroraMCAPI.getPlayer(pl);
            AuroraMCAPI.closeGUI(player);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player player = (Player) e.getWhoClicked();
            GUI gui = AuroraMCAPI.getGUI(AuroraMCAPI.getPlayer(player));
            if (gui != null) {
                if (e.getSlot() < 0) {
                    return;
                }
                ItemStack itemStack = e.getInventory().getItem(e.getSlot());
                if (itemStack != null) {
                    if (itemStack.getType() != Material.AIR) {
                        if (e.getInventory().getType() != InventoryType.PLAYER && e.getInventory().getType() != InventoryType.CREATIVE) {
                            if (gui.cancelEvent()) {
                                e.setCancelled(true);
                            }
                            int row = e.getSlot() / 9;
                            int column = e.getSlot() % 9;
                            gui.onClick(row, column, itemStack);
                        }
                    }
                }
            }
        }
    }

}
