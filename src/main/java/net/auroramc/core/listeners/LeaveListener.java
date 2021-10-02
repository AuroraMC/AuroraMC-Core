package net.auroramc.core.listeners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.backend.communication.CommunicationUtils;
import net.auroramc.core.api.backend.communication.Protocol;
import net.auroramc.core.api.backend.communication.ProtocolMessage;
import net.auroramc.core.api.utils.TabCompleteInjector;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveListener implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        if (AuroraMCAPI.getPlayer(e.getPlayer()) == null) {
            return;
        }
        if (AuroraMCAPI.getPlayer(e.getPlayer()).getActiveReportTask() != null) {
            AuroraMCAPI.getPlayer(e.getPlayer()).getActiveReportTask().cancel();
        }

        AuroraMCAPI.getPlayer(e.getPlayer()).clearScoreboard();
        AuroraMCAPI.playerLeave(e.getPlayer());
        TabCompleteInjector.removePlayer(e.getPlayer());
        ProtocolMessage message = new ProtocolMessage(Protocol.PLAYER_COUNT_CHANGE, "Mission Control", "leave", AuroraMCAPI.getServerInfo().getName(), AuroraMCAPI.getServerInfo().getNetwork().name() + "\n" + AuroraMCAPI.getServerInfo().getServerType().getString("game"));
        CommunicationUtils.sendMessage(message);

        /* if (!AuroraMCAPI.getPlayer(e.getPlayer()).isVanished()) {
            for (Player player2 : Bukkit.getOnlinePlayers()) {
                player2.sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Quit", e.getPlayer().getName()));
            }
        }*/
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        if (AuroraMCAPI.getPlayer(e.getPlayer()) == null) {
            return;
        }
        if (AuroraMCAPI.getPlayer(e.getPlayer()).getActiveReportTask() != null) {
            AuroraMCAPI.getPlayer(e.getPlayer()).getActiveReportTask().cancel();
        }

        AuroraMCAPI.getPlayer(e.getPlayer()).clearScoreboard();
        AuroraMCAPI.playerLeave(e.getPlayer());
        TabCompleteInjector.removePlayer(e.getPlayer());

        ProtocolMessage message = new ProtocolMessage(Protocol.PLAYER_COUNT_CHANGE, "Mission Control", "leave", AuroraMCAPI.getServerInfo().getName(), AuroraMCAPI.getServerInfo().getNetwork().name() + "\n" + AuroraMCAPI.getServerInfo().getServerType().getString("game"));
        CommunicationUtils.sendMessage(message);

        /* if (!AuroraMCAPI.getPlayer(e.getPlayer()).isVanished()) {
            for (Player player2 : Bukkit.getOnlinePlayers()) {
                player2.sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Quit", e.getPlayer().getName()));
            }
        }*/
    }

}
