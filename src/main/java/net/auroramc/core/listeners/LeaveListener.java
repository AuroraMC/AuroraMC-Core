package net.auroramc.core.listeners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.utils.TabCompleteInjector;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveListener implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        AuroraMCAPI.getPlayer(e.getPlayer()).clearScoreboard();
        AuroraMCAPI.playerLeave(e.getPlayer());
        TabCompleteInjector.removePlayer(e.getPlayer());

        if (!AuroraMCAPI.getPlayer(e.getPlayer()).isVanished()) {
            for (Player player2 : Bukkit.getOnlinePlayers()) {
                player2.sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Quit", e.getPlayer().getName()));
            }
        }
    }

}
