package network.auroramc.core.listeners;

import network.auroramc.core.api.AuroraMCAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveListener implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        AuroraMCAPI.playerLeave(e.getPlayer());
        TabCompleteInjector.removePlayer(e.getPlayer());
    }

}
