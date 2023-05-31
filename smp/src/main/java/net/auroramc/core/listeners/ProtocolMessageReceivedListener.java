/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.core.listeners;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.backend.ChatLogs;
import net.auroramc.api.backend.info.ServerInfo;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.player.ChatChannel;
import net.auroramc.api.player.PlayerPreferences;
import net.auroramc.api.utils.SMPLocation;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.backend.communication.CommunicationUtils;
import net.auroramc.core.api.backend.communication.Protocol;
import net.auroramc.core.api.backend.communication.ProtocolMessage;
import net.auroramc.core.api.events.server.ProtocolMessageEvent;
import net.auroramc.core.api.events.server.ServerCloseRequestEvent;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.player.SMPPlayer;
import net.auroramc.core.api.player.team.SMPTeam;
import net.auroramc.core.api.utils.ServerChatUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.json.JSONObject;

import java.util.UUID;

public class ProtocolMessageReceivedListener implements Listener {

    @EventHandler
    public void onMessageReceived(ProtocolMessageEvent e) {
        switch (e.getMessage().getProtocol()) {
            case UPDATE_RULES -> {
                //Reload the rules.
                AuroraMCAPI.loadRules();
            }
            case UPDATE_PLAYER_COUNT -> {
                //Respond with the current player count.
                int amount = Bukkit.getOnlinePlayers().size();
                ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_PLAYER_COUNT, "Mission Control", "reply", AuroraMCAPI.getInfo().getName(), amount + "\n" + AuroraMCAPI.getInfo().getNetwork().name() + "\n" + ((ServerInfo) AuroraMCAPI.getInfo()).getServerType().getString("game"));
                CommunicationUtils.sendMessage(message);
            }
            case SHUTDOWN -> {
                //Queue server for shutdown. Because this differs from server-to-server, implementation is set by the game engine/lobby/build core.
                ServerCloseRequestEvent event = new ServerCloseRequestEvent(false, e.getMessage().getCommand());
                Bukkit.getPluginManager().callEvent(event);
            }
            case MESSAGE -> {
                String raw = e.getMessage().getExtraInfo();
                String to = e.getMessage().getCommand();
                String sender = e.getMessage().getSender();
                if (sender.equalsIgnoreCase("Mission Control")) {
                    BaseComponent message = TextFormatter.pluginMessage("Mission Control", raw);
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(UUID.fromString(to));
                    if (player != null) {
                        player.sendMessage(message);
                    }
                } else if (to.equals("kill") && sender.startsWith("SMP")) {
                    for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
                        player.sendMessage(TextFormatter.pluginMessage("Kill", raw));
                    }
                } else if (to.equals("chat") && sender.startsWith("SMP")) {
                    String[] msg = raw.split(";");
                    BaseComponent component = ComponentSerializer.parse(msg[0])[0];
                    String filtered = msg[1];
                    int id = Integer.parseInt(msg[2]);
                    String name = msg[3];
                    Rank rank = Rank.valueOf(msg[4]);
                    for (AuroraMCServerPlayer recipient : ServerAPI.getPlayers()) {
                        if (recipient.getPreferences().isChatVisibilityEnabled()) {
                            if (!recipient.isIgnored(id) || recipient.hasPermission("moderation")) {
                                recipient.sendMessage(component);
                            }
                        }
                    }
                    ChatLogs.chatMessage(id, name, rank, filtered, false, ChatChannel.ALL, -1, null, null);
                } else {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(to);
                    if (player != null) {
                        BaseComponent message = TextFormatter.privateMessage("Mission Control", player, new TextComponent(raw));
                        player.sendMessage(message);
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 100);
                    }
                }
            }
            case STAFF_MESSAGE -> {
                String message = e.getMessage().getExtraInfo();
                String to = e.getMessage().getCommand();
                String sender = e.getMessage().getSender();
                AuroraMCServerPlayer target = ServerAPI.getPlayer(UUID.fromString(to));
                if (target != null) {
                    UUID from = UUID.fromString(sender);
                    Rank rank = AuroraMCAPI.getDbManager().getRank(from);
                    String name = AuroraMCAPI.getDbManager().getNameFromUUID(sender);
                    target.sendMessage(TextFormatter.formatStaffMessageFrom(rank, name, message));
                    target.playSound(target.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 100, 2);
                    BaseComponent formatted = TextFormatter.formatStaffMessage(rank, name, target, message);
                    for (AuroraMCServerPlayer p : ServerAPI.getPlayers()) {
                        if (p != target) {
                            if (p.hasPermission("moderation")) {
                                p.sendMessage(formatted);
                            }
                        }
                    }
                }
            }
            case EMERGENCY_SHUTDOWN -> {
                //Shutdown the server immediately. Because this differs from server-to-server, implementation is set by the game engine/lobby/build core.
                ServerCloseRequestEvent event = new ServerCloseRequestEvent(true, e.getMessage().getCommand());
                Bukkit.getPluginManager().callEvent(event);
            }
            case ALPHA_CHANGE -> {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.kickPlayer(TextFormatter.pluginMessageRaw("Server Manager", "&cThe alpha network has been closed.\n" +
                            "\n" +
                            "&rThanks for helping us test out our upcoming updates!\n" +
                            "\n" +
                            "Keep an eye on auroramc.net and our Discord server for information on when the network will be open again!"));
                }
                CommunicationUtils.sendMessage(new ProtocolMessage(Protocol.CONFIRM_SHUTDOWN, "Mission Control", "shutdown", AuroraMCAPI.getInfo().getName(), ""));
                CommunicationUtils.shutdown();
            }
            case UPDATE_MAPS -> {
                String [] args = e.getMessage().getExtraInfo().split(";");
                switch (e.getMessage().getCommand()) {
                    case "setleader" -> {
                        UUID uuid = UUID.fromString(args[0]);
                        if (ServerAPI.getLoadedTeams().containsKey(uuid)) {
                            UUID uuid2 = UUID.fromString(args[1]);
                            SMPTeam team = ServerAPI.getLoadedTeams().get(uuid);
                            team.setLeader(team.getMember(uuid2), false);
                        }
                    }
                    case "addmember" -> {
                        UUID uuid = UUID.fromString(args[0]);
                        if (ServerAPI.getLoadedTeams().containsKey(uuid)) {
                            UUID uuid2 = UUID.fromString(args[1]);
                            int id = Integer.parseInt(args[2]);
                            String name = args[3];
                            Rank rank = Rank.valueOf(args[4]);
                            SMPTeam team = ServerAPI.getLoadedTeams().get(uuid);

                            team.addMember(new SMPPlayer(id, name, uuid2, null, rank), false);
                        }
                    }
                    case "removemember" -> {
                        UUID uuid = UUID.fromString(args[0]);
                        if (ServerAPI.getLoadedTeams().containsKey(uuid)) {
                            UUID uuid2 = UUID.fromString(args[1]);
                            SMPTeam team = ServerAPI.getLoadedTeams().get(uuid);
                            team.removeMember(uuid2, false);
                        }
                    }
                    case "setname" -> {
                        UUID uuid = UUID.fromString(args[0]);
                        if (ServerAPI.getLoadedTeams().containsKey(uuid)) {
                            String name = args[1];
                            SMPTeam team = ServerAPI.getLoadedTeams().get(uuid);
                            team.setName(name, false);
                        }
                    }
                    case "sethome" -> {
                        UUID uuid = UUID.fromString(args[0]);
                        if (ServerAPI.getLoadedTeams().containsKey(uuid)) {
                            SMPTeam team = ServerAPI.getLoadedTeams().get(uuid);
                            team.setHome(SMPLocation.fromJSON(new JSONObject(args[1])), false);
                        }
                    }
                    case "disband" -> {
                        UUID uuid = UUID.fromString(args[0]);
                        if (ServerAPI.getLoadedTeams().containsKey(uuid)) {
                            SMPTeam team = ServerAPI.getLoadedTeams().get(uuid);
                            team.disband(false);
                        }
                    }
                }
            }
        }
    }

}
