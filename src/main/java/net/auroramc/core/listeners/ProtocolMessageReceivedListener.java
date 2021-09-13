package net.auroramc.core.listeners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.backend.communication.CommunicationUtils;
import net.auroramc.core.api.backend.communication.Protocol;
import net.auroramc.core.api.backend.communication.ProtocolMessage;
import net.auroramc.core.api.events.ProtocolMessageEvent;
import net.auroramc.core.api.events.ServerCloseRequestEvent;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

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
                ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_PLAYER_COUNT, "Mission Control", "reply", AuroraMCAPI.getServerInfo().getName(), amount + "\n" + AuroraMCAPI.getServerInfo().getNetwork().name() + "\n" + AuroraMCAPI.getServerInfo().getServerType().getString("game"));
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
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(UUID.fromString(to));
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
            case STAFF_MESSAGE: {
                String message = e.getMessage().getExtraInfo();
                String to = e.getMessage().getCommand();
                String sender = e.getMessage().getSender();
                AuroraMCPlayer target = AuroraMCAPI.getPlayer(UUID.fromString(to));
                if (target != null) {
                    UUID from = UUID.fromString(sender);
                    Rank rank = AuroraMCAPI.getDbManager().getRank(from);
                    String name = AuroraMCAPI.getDbManager().getNameFromUUID(sender);
                    target.getPlayer().spigot().sendMessage(AuroraMCAPI.getFormatter().formatStaffMessageFrom(rank, name, message));
                    target.getPlayer().playSound(target.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 2);
                    BaseComponent formatted = AuroraMCAPI.getFormatter().formatStaffMessage(rank, name, target, message);
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p != target.getPlayer()) {
                            if (AuroraMCAPI.getPlayer(p) != null) {
                                if (AuroraMCAPI.getPlayer(p).hasPermission("moderation")) {
                                    p.spigot().sendMessage(formatted);
                                }
                            }
                        }
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
