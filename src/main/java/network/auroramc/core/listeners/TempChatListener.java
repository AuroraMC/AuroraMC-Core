package network.auroramc.core.listeners;

import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class TempChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        if (AuroraMCAPI.getPlayer(e.getPlayer()).isVanished()) {
            e.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Vanish", "You cannot talk while vanished!"));
            return;
        }
        switch (AuroraMCAPI.getPlayer(e.getPlayer()).getChannel()) {
            case STAFF:
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(e.getPlayer());
                player.getPlayer().spigot().sendMessage(AuroraMCAPI.getFormatter().formatStaffMessage(player, e.getMessage()));
                player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 2);
                for (Player target : Bukkit.getOnlinePlayers()) {
                    AuroraMCPlayer p = AuroraMCAPI.getPlayer(target);
                    if (p != null) {
                        if (p != player) {
                            if (p.hasPermission("moderation")) {
                                p.getPlayer().spigot().sendMessage(AuroraMCAPI.getFormatter().formatStaffMessage(player, e.getMessage()));
                                p.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 2);
                            }
                        }

                    }
                }
                break;
            case ALL:
            case PARTY:
                e.setMessage(AuroraMCAPI.getFilter().filter(e.getMessage()));
                for (Player player2 : Bukkit.getOnlinePlayers()) {
                    player2.spigot().sendMessage(AuroraMCAPI.getFormatter().chatMessage(AuroraMCAPI.getPlayer(e.getPlayer()), e.getMessage()));
                }
                break;
        }

    }
}
