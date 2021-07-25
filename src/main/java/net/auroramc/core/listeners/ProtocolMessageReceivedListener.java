package net.auroramc.core.listeners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.backend.communication.CommunicationUtils;
import net.auroramc.core.api.backend.communication.Protocol;
import net.auroramc.core.api.backend.communication.ProtocolMessage;
import net.auroramc.core.api.events.ProtocolMessageEvent;
import net.auroramc.core.api.events.ServerCloseRequestEvent;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ProtocolMessageReceivedListener implements Listener {

    @EventHandler
    public void onMessageReceived(ProtocolMessageEvent e) {
        switch (e.getMessage().getProtocol()) {
            case UPDATE_RULES: {
                //Reload the rules.
                AuroraMCAPI.loadRules();
            }
            case UPDATE_PLAYER_COUNT: {
                //Respond with the current player count.
                int amount = Bukkit.getOnlinePlayers().size();
                ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_PLAYER_COUNT, "Mission Control", "reply", AuroraMCAPI.getServerInfo().getName(), "" + amount);
                CommunicationUtils.sendMessage(message);
            }
            case SHUTDOWN: {
                //Queue server for shutdown. Because this differs from server-to-server, implementation is set by the game engine/lobby/build core.
                ServerCloseRequestEvent event = new ServerCloseRequestEvent(false, e.getMessage().getCommand());
                Bukkit.getPluginManager().callEvent(event);
            }
            case MESSAGE: {
                String message = e.getMessage().getExtraInfo();
                String to = e.getMessage().getCommand();
                String sender = e.getMessage().getSender();
                if (sender.equalsIgnoreCase("Mission Control")) {
                    message = AuroraMCAPI.getFormatter().pluginMessage("Mission Control", message);
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(to);
                    if (player != null) {
                        player.getPlayer().sendMessage(message);
                    }
                } else {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(to);
                    if (player != null) {
                        message = AuroraMCAPI.getFormatter().privateMessage("Mission Control", player, message);
                        player.getPlayer().sendMessage(message);
                        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 1, 100);
                    }
                }
            }
            case EMERGENCY_SHUTDOWN: {
                //Shutdown the server immediately. Because this differs from server-to-server, implementation is set by the game engine/lobby/build core.
                ServerCloseRequestEvent event = new ServerCloseRequestEvent(true, e.getMessage().getCommand());
                Bukkit.getPluginManager().callEvent(event);
            }
        }
    }

}
