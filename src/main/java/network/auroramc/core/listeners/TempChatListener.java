package network.auroramc.core.listeners;

import network.auroramc.core.api.AuroraMCAPI;
import org.bukkit.Bukkit;
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
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.spigot().sendMessage(AuroraMCAPI.getFormatter().chatMessage(AuroraMCAPI.getPlayer(e.getPlayer()), e.getMessage()));
        }
    }
}
