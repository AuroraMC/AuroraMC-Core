/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.proxy.listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.backend.ChatLogs;
import net.auroramc.api.backend.info.ServerInfo;
import net.auroramc.api.cosmetics.Cosmetic;
import net.auroramc.api.cosmetics.FriendStatus;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.permissions.SubRank;
import net.auroramc.api.player.IgnoredPlayer;
import net.auroramc.api.player.PlayerPreferences;
import net.auroramc.api.player.PlayerReport;
import net.auroramc.api.player.friends.Friend;
import net.auroramc.api.player.friends.FriendsList;
import net.auroramc.api.punishments.Punishment;
import net.auroramc.api.punishments.PunishmentLength;
import net.auroramc.api.punishments.Rule;
import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.SMPLocation;
import net.auroramc.api.utils.Pronoun;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyAPI;
import net.auroramc.proxy.api.backend.ProxyDatabaseManager;
import net.auroramc.proxy.api.backend.communication.CommunicationUtils;
import net.auroramc.proxy.api.backend.communication.Protocol;
import net.auroramc.proxy.api.backend.communication.ProtocolMessage;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import net.auroramc.proxy.api.utils.PanelAccountType;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class RecievePluginMessage implements Listener {

    @EventHandler
    public void onPluginMessage(PluginMessageEvent e) {
        //process anything that is sent by the server and exclusively by a server.
        if (!(e.getSender() instanceof Server)) {
            return;
        }
        if (e.getTag().equals("BungeeCord")) {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(e.getData()));
            try {
                String channel = in.readUTF(); // channel we delivered
                ProxyAPI.getCore().getLogger().info("Plugin message received, channel: " + channel);
                switch (channel) {
                    case "Mute": {
                        //This is a mute, process the mute.
                        String code = in.readUTF();
                        ProxyAPI.getCore().getProxy().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                            Punishment punishment = AuroraMCAPI.getDbManager().getPunishment(code);
                            Rule rule = AuroraMCAPI.getRules().getRule(punishment.getRuleID());
                            PunishmentLength length = new PunishmentLength((punishment.getExpire() - System.currentTimeMillis())/3600000d);
                            if (punishment.getStatus() == 2) {
                                ProxyAPI.getCore().getProxy().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                                    ProtocolMessage message = new ProtocolMessage(Protocol.APPROVAL_NOTIFICATION, "Mission Control", "", AuroraMCAPI.getInfo().getName(), "\n" + AuroraMCAPI.getInfo().getNetwork().name());
                                    CommunicationUtils.sendMessage(message);
                                });
                            }
                            ProxiedPlayer player = ProxyAPI.getCore().getProxy().getPlayer(punishment.getPunishedName());
                            if (player != null) {
                                if (player.isConnected()) {
                                    if (punishment.getStatus() == 2) {
                                        player.sendMessage(TextFormatter.pluginMessage("Punishments",String.format("You have been muted for **%s**.\n" +
                                                "Reason: **%s - %s [Awaiting Approval]**\n" +
                                                "Punishment Code: **%s**\n\n" +
                                                "Your punishment has been applied by a Junior Moderator, and is severe enough to need approval from a Staff Management member to ensure that the punishment applied was correct. When it is approved, the full punishment length will automatically apply to you. If this punishment is denied for being false, **it will automatically be removed**, and the punishment will automatically be removed from your Punishment History.", length.getFormatted(), rule.getRuleName(), punishment.getExtraNotes(), code)));
                                    } else {
                                        player.sendMessage(TextFormatter.pluginMessage("Punish", String.format("You have been muted for **%s**.\n" +
                                                "Reason: **%s - %s**\n" +
                                                "Punishment Code: **%s**", length.getFormatted(), rule.getRuleName(), punishment.getExtraNotes(), code)));
                                    }
                                    ProxyAPI.getPlayer(player).applyMute(punishment);

                                }
                            } else {
                                UUID uuid = AuroraMCAPI.getDbManager().getUUID(punishment.getPunished());
                                if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                                    ProtocolMessage message = new ProtocolMessage(Protocol.PUNISH, AuroraMCAPI.getDbManager().getProxy(uuid).toString(), "mute", AuroraMCAPI.getInfo().getName(), punishment.getPunishmentCode());
                                    CommunicationUtils.sendMessage(message);
                                }
                            }
                        });
                        break;
                    }
                    case "Ban": {
                        String code = in.readUTF();
                        ProxyAPI.getCore().getProxy().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                            Punishment punishment = AuroraMCAPI.getDbManager().getPunishment(code);
                            Rule rule = AuroraMCAPI.getRules().getRule(punishment.getRuleID());
                            PunishmentLength length = new PunishmentLength((punishment.getExpire() - System.currentTimeMillis())/3600000d);
                            if (punishment.getStatus() == 2) {
                                List<AuroraMCProxyPlayer> players = ProxyAPI.getPlayers().stream().filter(AuroraMCProxyPlayer -> AuroraMCProxyPlayer.hasPermission("staffmanagement")).filter(AuroraMCProxyPlayer -> AuroraMCProxyPlayer.getPreferences().isApprovalNotificationsEnabled()).collect(Collectors.toList());
                                for (AuroraMCProxyPlayer pl : players) {
                                    pl.sendMessage(TextFormatter.pluginMessage("Punish", "There is a new punishment to be approved. Type /sm to view all punishments awaiting approval."));
                                }
                            }
                            ProxiedPlayer player = ProxyAPI.getCore().getProxy().getPlayer(punishment.getPunishedName());
                            if (player != null) {
                                if (player.isConnected()) {
                                    switch (punishment.getStatus()) {
                                        case 2:
                                            player.disconnect(TextFormatter.pluginMessage("Punishments",String.format("You have been banned from the network.\n" +
                                                    "\n" +
                                                    "&rReason: **%s - %s [Awaiting Approval]**\n" +
                                                    "&rExpires:  **%s**\n" +
                                                    "\n" +
                                                    "&rPunishment Code: **%s**\n" +
                                                    "\n" +
                                                    "&rYour punishment has been applied by a Trial Moderator, and is severe enough to need approval\n" +
                                                    "&rfrom a Staff Management member to ensure that the punishment applied was correct. When it is\n" +
                                                    "&rapproved, the full punishment length will automatically apply to you. If this punishment is\n" +
                                                    "&rdenied for being false, **it will automatically be removed**, and the punishment will automatically\n" +
                                                    "&rbe removed from your Punishment History.\n" +
                                                    "\n" +
                                                    "&rIf you believe this was a false punishment, please appeal at appeal.auroramc.net.", rule.getRuleName(), punishment.getExtraNotes(), length.getFormatted(), punishment.getPunishmentCode())));
                                            break;
                                        case 3:
                                            player.disconnect(TextFormatter.pluginMessage("Punishments",String.format("You have been banned from the network.\n" +
                                                    "\n" +
                                                    "&rReason: **%s - %s [SM Approved]**\n" +
                                                    "&rExpires:  **%s**\n" +
                                                    "\n" +
                                                    "&rPunishment Code: **%s**\n" +
                                                    "\n" +
                                                    "&rIf you believe this was a false punishment, please appeal at appeal.auroramc.net.", rule.getRuleName(), punishment.getExtraNotes(), length.getFormatted(), punishment.getPunishmentCode())));
                                            break;
                                        default:
                                            player.disconnect(TextFormatter.pluginMessage("Punishments",String.format("You have been banned from the network.\n" +
                                                    "\n" +
                                                    "&rReason: **%s - %s**\n" +
                                                    "&rExpires:  **%s**\n" +
                                                    "\n" +
                                                    "&rPunishment Code: **%s**\n" +
                                                    "\n" +
                                                    "&rIf you believe this was a false punishment, please appeal at appeal.auroramc.net.", rule.getRuleName(), punishment.getExtraNotes(), length.getFormatted(), punishment.getPunishmentCode())));
                                            break;
                                    }
                                }
                            } else {
                                UUID uuid = AuroraMCAPI.getDbManager().getUUID(punishment.getPunished());
                                if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                                    ProtocolMessage message = new ProtocolMessage(Protocol.PUNISH, AuroraMCAPI.getDbManager().getProxy(uuid).toString(), "ban", AuroraMCAPI.getInfo().getName(), punishment.getPunishmentCode());
                                    CommunicationUtils.sendMessage(message);
                                }
                            }
                        });
                        break;
                    }
                    case "Warning": {
                        String code = in.readUTF();
                        ProxyAPI.getCore().getProxy().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                            Punishment punishment = AuroraMCAPI.getDbManager().getPunishment(code);
                            Rule rule = AuroraMCAPI.getRules().getRule(punishment.getRuleID());
                            ProxiedPlayer player = ProxyAPI.getCore().getProxy().getPlayer(punishment.getPunishedName());
                            if (player != null) {
                                if (player.isConnected()) {
                                    player.sendMessage(TextFormatter.pluginMessage("Punishments",String.format("You have been issued a warning.\n" +
                                            "Reason: **%s - %s**\n" +
                                            "Punishment Code: **%s**", rule.getRuleName(), punishment.getExtraNotes(), punishment.getPunishmentCode())));
                                }
                            } else {
                                UUID uuid = AuroraMCAPI.getDbManager().getUUID(punishment.getPunished());
                                if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                                    ProtocolMessage message = new ProtocolMessage(Protocol.PUNISH, AuroraMCAPI.getDbManager().getProxy(uuid).toString(), "warning", AuroraMCAPI.getInfo().getName(), punishment.getPunishmentCode());
                                    CommunicationUtils.sendMessage(message);
                                }
                            }
                        });
                        break;
                    }
                    case "ApprovalBan": {
                        //This is an approval ban, process the mute.
                        ProxyAPI.getCore().getProxy().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                                ProtocolMessage message = new ProtocolMessage(Protocol.APPROVAL_NOTIFICATION, "Mission Control", "", AuroraMCAPI.getInfo().getName(), "\n" + AuroraMCAPI.getInfo().getNetwork().name());
                                CommunicationUtils.sendMessage(message);
                        });
                        break;
                    }
                    case "Unmute":
                    case "SMDenial": {
                        //This is a mute, process the mute.
                        String code = in.readUTF();
                        ProxyAPI.getCore().getProxy().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                            Punishment punishment = AuroraMCAPI.getDbManager().getPunishment(code);
                            ProxiedPlayer player = ProxyAPI.getCore().getProxy().getPlayer(punishment.getPunishedName());
                            if (player != null) {
                                if (player.isConnected()) {
                                    ProxyAPI.getPlayer(player).removeMute(punishment);
                                }
                            } else {
                                UUID uuid = AuroraMCAPI.getDbManager().getUUID(punishment.getPunished());
                                if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                                    ProtocolMessage message = new ProtocolMessage(Protocol.PUNISH, AuroraMCAPI.getDbManager().getProxy(uuid).toString(), "unmute", AuroraMCAPI.getInfo().getName(), punishment.getPunishmentCode());
                                    CommunicationUtils.sendMessage(message);
                                }
                            }
                        });
                        break;
                    }
                    case "SMApproval": {
                        //This is a mute, process the mute.
                        String code = in.readUTF();
                        ProxyAPI.getCore().getProxy().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                            Punishment punishment = AuroraMCAPI.getDbManager().getPunishment(code);
                            ProxiedPlayer player = ProxyAPI.getCore().getProxy().getPlayer(punishment.getPunishedName());
                            if (player != null) {
                                if (player.isConnected()) {
                                    ProxyAPI.getPlayer(player).punishmentApproved(punishment);
                                }
                            } else {
                                UUID uuid = AuroraMCAPI.getDbManager().getUUID(punishment.getPunished());
                                if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                                    ProtocolMessage message = new ProtocolMessage(Protocol.PUNISH, AuroraMCAPI.getDbManager().getProxy(uuid).toString(), "approved", AuroraMCAPI.getInfo().getName(), punishment.getPunishmentCode());
                                    CommunicationUtils.sendMessage(message);
                                }
                            }
                        });
                        break;
                    }
                    case "Disguise": {
                        String player = in.readUTF();
                        String name = in.readUTF();
                        String skin = in.readUTF();
                        int rank = in.readInt();

                        ProxyAPI.getPlayer(ProxyAPI.getCore().getProxy().getPlayer(player)).disguise(skin, name, Rank.getByID(rank));
                        break;
                    }
                    case "UnDisguise": {
                        String player = in.readUTF();

                        ProxyAPI.getPlayer(ProxyAPI.getCore().getProxy().getPlayer(player)).undisguise(false);
                        break;
                    }
                    case "XPAdd": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        player.getStats().addXp(in.readLong(), false);
                        break;
                    }
                    case "StatisticIncrement": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        int gameId = in.readInt();
                        String key = in.readUTF();
                        long amount = in.readLong();
                        player.getStats().incrementStatistic(gameId, key, amount, false);
                        break;
                    }
                    case "JoinGame": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        String server = in.readUTF();
                        ServerInfo info = ProxyAPI.getAmcServers().get(server);
                        player.connect(info);
                        player.sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("You are being sent from **%s** to **%s**.", player.getServer().getName(), info.getName())));
                        if (player.getParty() != null) {
                            player.getParty().warp(info);
                        }
                        break;
                    }
                    case "AchievementGained": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        Achievement achievement = AuroraMCAPI.getAchievement(in.readInt());
                        int tier = in.readInt();
                        player.getStats().achievementGained(achievement, tier, false);
                        break;
                    }
                    case "AchievementProgress": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(in.readUTF());
                        Achievement achievement = AuroraMCAPI.getAchievement(in.readInt());
                        long amount = in.readLong();
                        player.getStats().addProgress(achievement, amount, player.getStats().getAchievementsGained().getOrDefault(achievement, 0), false);
                        break;
                    }
                    case "AchievementProgressTierGained": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        Achievement achievement = AuroraMCAPI.getAchievement(in.readInt());
                        long amount = in.readLong();
                        int tier = in.readInt();
                        player.getStats().addProgress(achievement, amount, player.getStats().getAchievementsGained().getOrDefault(achievement, 0), false);
                        break;
                    }
                    case "GameWon": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        player.getStats().addGamePlayed(true, false);
                        break;
                    }
                    case "GameLost": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        player.getStats().addGamePlayed(false, false);
                        break;
                    }
                    case "CrownsEarned": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        long amount = in.readLong();
                        player.getStats().addCrownsEarned(amount, false);
                        break;
                    }
                    case "TicketsEarned": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        long amount = in.readLong();
                        player.getStats().addTicketsEarned(amount, false);
                        break;
                    }
                    case "GameTimeAdded": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        long amount = in.readLong();
                        player.getStats().addGameTime(amount, false);
                        break;
                    }
                    case "LobbyTimeAdded": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        long amount = in.readLong();
                        player.getStats().addLobbyTime(amount, false);
                        break;
                    }
                    case "TicketsAdded": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        long amount = in.readLong();
                        player.getBank().addTickets(amount, in.readBoolean(), false);
                        break;
                    }
                    case "CrownsAdded": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        long amount = in.readLong();
                        player.getBank().addCrowns(amount, in.readBoolean(), false);
                        break;
                    }
                    case "WithdrawCrowns": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        long amount = in.readLong();
                        player.getBank().withdrawCrowns(amount, in.readBoolean(), false);
                        break;
                    }
                    case "WithdrawTickets": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        long amount = in.readLong();
                        player.getBank().withdrawTickets(amount, in.readBoolean(), false);
                        break;
                    }
                    case "CrownsRemoved": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        long amount = in.readLong();
                        player.getStats().removeCrownsEarned(amount, false);
                        break;
                    }
                    case "TicketsRemoved": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        long amount = in.readLong();
                        player.getStats().removeTicketsEarned(amount, false);
                        break;
                    }
                    case "SetRank": {
                        UUID uuid = UUID.fromString(in.readUTF());
                        Rank rank = Rank.getByID(in.readInt());
                        assert rank != null;
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(uuid);
                        if (player != null) {
                            if (player.getRank() == Rank.MODERATOR) {
                                AuroraMCAPI.getDbManager().modLeave(player.getName());
                            } else if (player.getRank() == Rank.JUNIOR_MODERATOR) {
                                AuroraMCAPI.getDbManager().jrModLeave(player.getName());
                            }

                            player.setRank(rank);

                            if (rank.hasPermission("panel")) {
                                if (AuroraMCAPI.getDbManager().hasAccount(player.getUniqueId())) {
                                    PanelAccountType panelAccountType = null;
                                    for (PanelAccountType type : PanelAccountType.values()) {
                                        if (type.getRank() != null) {
                                            if (player.getRank() == type.getRank()) {
                                                panelAccountType = type;
                                                break;
                                            }
                                        }
                                        if (type.getSubrank() != null) {
                                            if (player.getSubranks().contains(type.getSubrank())) {
                                                panelAccountType = type;
                                                break;
                                            }
                                        }
                                    }
                                    if (panelAccountType != null) {
                                        ProxyDatabaseManager.updatePanelAccountType(player.getUniqueId(), panelAccountType);
                                    } else  {
                                        AuroraMCAPI.getDbManager().deletePanelAccount(player.getUniqueId());
                                    }
                                }
                            }
                            if (rank == Rank.MODERATOR) {
                                AuroraMCAPI.getDbManager().modJoin(player.getName());
                            } else if (rank == Rank.JUNIOR_MODERATOR) {
                                AuroraMCAPI.getDbManager().modLeave(player.getName());
                            }
                            player.sendMessage(TextFormatter.pluginMessage("Permissions", String.format("Your rank was set to **%s**.", rank.getName())));

                            ByteArrayDataOutput out = ByteStreams.newDataOutput();
                            out.writeUTF("SetRank");
                            out.writeUTF(player.getUniqueId().toString());
                            out.writeInt(rank.getId());
                            player.sendPluginMessage(out.toByteArray());
                        } else {
                            if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                                ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_PROFILE, AuroraMCAPI.getDbManager().getProxy(uuid).toString(), "set_rank", AuroraMCAPI.getInfo().getName(), rank.getId() + "\n" + uuid.toString());
                                CommunicationUtils.sendMessage(message);
                            } else {
                                if (AuroraMCAPI.getDbManager().hasAccount(uuid)) {
                                    List<SubRank> subranks = AuroraMCAPI.getDbManager().getSubRanks(uuid);
                                    PanelAccountType panelAccountType = null;
                                    for (PanelAccountType type : PanelAccountType.values()) {
                                        if (type.getRank() != null) {
                                            if (rank == type.getRank()) {
                                                panelAccountType = type;
                                                break;
                                            }
                                        }
                                        if (type.getSubrank() != null) {
                                            if (subranks.contains(type.getSubrank())) {
                                                panelAccountType = type;
                                                break;
                                            }
                                        }
                                    }
                                    if (panelAccountType != null) {
                                        ProxyDatabaseManager.updatePanelAccountType(uuid, panelAccountType);
                                    } else  {
                                        AuroraMCAPI.getDbManager().deletePanelAccount(uuid);
                                    }
                                }
                            }
                        }
                        break;
                    }
                    case "AddSubRank": {
                        UUID uuid = UUID.fromString(in.readUTF());
                        SubRank rank = SubRank.getByID(in.readInt());
                        assert rank != null;
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(uuid);
                        if (player != null) {
                            player.grantSubrank(rank);
                            if (rank.hasPermission("panel")) {
                                if (AuroraMCAPI.getDbManager().hasAccount(player.getUniqueId())) {
                                    PanelAccountType panelAccountType = null;
                                    for (PanelAccountType type : PanelAccountType.values()) {
                                        if (type.getRank() != null) {
                                            if (player.getRank() == type.getRank()) {
                                                panelAccountType = type;
                                                break;
                                            }
                                        }
                                        if (type.getSubrank() != null) {
                                            if (player.getSubranks().contains(type.getSubrank())) {
                                                panelAccountType = type;
                                                break;
                                            }
                                        }
                                    }
                                    if (panelAccountType != null) {
                                        ProxyDatabaseManager.updatePanelAccountType(player.getUniqueId(), panelAccountType);
                                    } else  {
                                        AuroraMCAPI.getDbManager().deletePanelAccount(player.getUniqueId());
                                    }
                                }
                            }
                            player.sendMessage(TextFormatter.pluginMessage("Permissions", String.format("The SubRank **%s** has been added to your account.", rank.getName())));
                        } else {
                            if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                                ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_PROFILE, AuroraMCAPI.getDbManager().getProxy(uuid).toString(), "add_subrank", AuroraMCAPI.getInfo().getName(), rank.getId() + "\n" + uuid.toString());
                                CommunicationUtils.sendMessage(message);
                            } else {
                                if (AuroraMCAPI.getDbManager().hasAccount(uuid)) {
                                    Rank mainrank = AuroraMCAPI.getDbManager().getRank(uuid);
                                    List<SubRank> subranks = AuroraMCAPI.getDbManager().getSubRanks(uuid);
                                    PanelAccountType panelAccountType = null;
                                    for (PanelAccountType type : PanelAccountType.values()) {
                                        if (type.getRank() != null) {
                                            if (mainrank == type.getRank()) {
                                                panelAccountType = type;
                                                break;
                                            }
                                        }
                                        if (type.getSubrank() != null) {
                                            if (subranks.contains(type.getSubrank())) {
                                                panelAccountType = type;
                                                break;
                                            }
                                        }
                                    }
                                    if (panelAccountType != null) {
                                        ProxyDatabaseManager.updatePanelAccountType(uuid, panelAccountType);
                                    } else  {
                                        AuroraMCAPI.getDbManager().deletePanelAccount(uuid);
                                    }
                                }
                            }
                        }
                        break;
                    }
                    case "RemoveSubRank": {
                        UUID uuid = UUID.fromString(in.readUTF());
                        SubRank rank = SubRank.getByID(in.readInt());
                        assert rank != null;
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(uuid);
                        if (player != null) {
                            player.revokeSubrank(rank);
                            if (rank.hasPermission("panel")) {
                                if (AuroraMCAPI.getDbManager().hasAccount(player.getUniqueId())) {
                                    PanelAccountType panelAccountType = null;
                                    for (PanelAccountType type : PanelAccountType.values()) {
                                        if (type.getRank() != null) {
                                            if (player.getRank() == type.getRank()) {
                                                panelAccountType = type;
                                                break;
                                            }
                                        }
                                        if (type.getSubrank() != null) {
                                            if (player.getSubranks().contains(type.getSubrank())) {
                                                panelAccountType = type;
                                                break;
                                            }
                                        }
                                    }
                                    if (panelAccountType != null) {
                                        ProxyDatabaseManager.updatePanelAccountType(player.getUniqueId(), panelAccountType);
                                    } else  {
                                        AuroraMCAPI.getDbManager().deletePanelAccount(player.getUniqueId());
                                    }
                                }
                            }
                            player.sendMessage(TextFormatter.pluginMessage("Permissions", String.format("The SubRank **%s** has been removed from your account.", rank.getName())));
                        } else {
                            if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                                ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_PROFILE, AuroraMCAPI.getDbManager().getProxy(uuid).toString(), "remove_subrank", AuroraMCAPI.getInfo().getName(), rank.getId() + "\n" + uuid.toString());
                                CommunicationUtils.sendMessage(message);
                            } else {
                                if (AuroraMCAPI.getDbManager().hasAccount(uuid)) {
                                    Rank mainrank = AuroraMCAPI.getDbManager().getRank(uuid);
                                    List<SubRank> subranks = AuroraMCAPI.getDbManager().getSubRanks(uuid);
                                    PanelAccountType panelAccountType = null;
                                    for (PanelAccountType type : PanelAccountType.values()) {
                                        if (type.getRank() != null) {
                                            if (mainrank == type.getRank()) {
                                                panelAccountType = type;
                                                break;
                                            }
                                        }
                                        if (type.getSubrank() != null) {
                                            if (subranks.contains(type.getSubrank())) {
                                                panelAccountType = type;
                                                break;
                                            }
                                        }
                                    }
                                    if (panelAccountType != null) {
                                        ProxyDatabaseManager.updatePanelAccountType(uuid, panelAccountType);
                                    } else  {
                                        AuroraMCAPI.getDbManager().deletePanelAccount(uuid);
                                    }
                                }
                            }

                        }
                        break;
                    }
                    case "PlusColour": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        player.getActiveSubscription().setColor(ChatColor.of(in.readUTF()), false);
                        break;
                    }
                    case "LevelColour": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        player.getActiveSubscription().setLevelColor(ChatColor.of(in.readUTF()), false);
                        break;
                    }
                    case "SuffixColour": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        player.getActiveSubscription().setSuffixColor(ChatColor.of(in.readUTF()), false);
                        break;
                    }
                    case "AddPlusDays": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        int amount = in.readInt();
                        long endTimestamp = AuroraMCAPI.getDbManager().getExpire(player.getUniqueId());
                        ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                            if (endTimestamp != -1 && endTimestamp > System.currentTimeMillis()) {
                                //They already have an active subscription
                                AuroraMCAPI.getDbManager().extend(player.getUniqueId(), amount);
                                player.getActiveSubscription().extend(amount);
                            } else {
                                AuroraMCAPI.getDbManager().newSubscription(player.getUniqueId(), amount);
                                player.newSubscription();
                            }
                        });
                    }
                    case "FriendRequestAccepted": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        UUID uuid = UUID.fromString(in.readUTF());
                        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
                        if (target != null) {
                            AuroraMCProxyPlayer auroraMCProxyPlayer = ProxyAPI.getPlayer(target);
                            FriendsList.VisibilityMode visibilityMode = auroraMCProxyPlayer.getFriendsList().getVisibilityMode();
                            player.getFriendsList().friendRequestAccepted(uuid, true, ((visibilityMode == FriendsList.VisibilityMode.ALL) ? player.getServer().getName() : null), auroraMCProxyPlayer.getFriendsList().getCurrentStatus(), true);
                            visibilityMode = player.getFriendsList().getVisibilityMode();
                            auroraMCProxyPlayer.getFriendsList().friendRequestAccepted(player.getUniqueId(), true, ((visibilityMode == FriendsList.VisibilityMode.ALL) ? player.getServer().getName() : null), player.getFriendsList().getCurrentStatus(), true);
                        } else {
                            if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                                UUID proxy = AuroraMCAPI.getDbManager().getProxy(uuid);
                                ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_FRIENDS, proxy.toString(), "accepted", player.getUniqueId().toString(), uuid.toString());
                                CommunicationUtils.sendMessage(message);
                                FriendsList.VisibilityMode visibilityMode = AuroraMCAPI.getDbManager().getVisibilityMode(uuid);
                                FriendStatus status = AuroraMCAPI.getDbManager().getFriendStatus(uuid);
                                String server = AuroraMCAPI.getDbManager().getCurrentServer(uuid);
                                player.getFriendsList().friendRequestAccepted(uuid, true, ((visibilityMode != FriendsList.VisibilityMode.NOBODY)?((visibilityMode == FriendsList.VisibilityMode.FAVOURITE_FRIENDS_ONLY)?null:server):null), status, true);
                            } else {
                                player.getFriendsList().friendRequestAccepted(uuid, false, null, (FriendStatus) AuroraMCAPI.getCosmetics().get(101), true);
                            }
                        }
                        break;
                    }
                    case "FriendRequestDenied": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        UUID uuid = UUID.fromString(in.readUTF());
                        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
                        player.getFriendsList().friendRequestRemoved(uuid, false, true);
                        if (target != null) {
                            AuroraMCProxyPlayer AuroraMCProxyPlayer = ProxyAPI.getPlayer(target);
                            AuroraMCProxyPlayer.getFriendsList().friendRequestRemoved(player.getUniqueId(), true, false);
                        }
                        break;
                    }
                    case "FriendDeleted": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        UUID uuid = UUID.fromString(in.readUTF());
                        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
                        player.getFriendsList().friendRemoved(uuid, false, true);
                        if (target != null) {
                            AuroraMCProxyPlayer AuroraMCProxyPlayer = ProxyAPI.getPlayer(target);
                            AuroraMCProxyPlayer.getFriendsList().friendRemoved(player.getUniqueId(), true, false);
                        } else {
                            if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                                ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_FRIENDS, AuroraMCAPI.getDbManager().getProxy(uuid).toString(), "remove", player.getUniqueId().toString(), uuid.toString());
                                CommunicationUtils.sendMessage(message);
                            }
                        }
                        break;
                    }
                    case "FriendVisibilitySet": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        player.getFriendsList().setVisibilityMode(FriendsList.VisibilityMode.valueOf(in.readUTF()), false);
                        break;
                    }
                    case "FriendStatusSet": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        player.getFriendsList().setCurrentStatus((FriendStatus) AuroraMCAPI.getCosmetics().get(in.readInt()), false);
                        break;
                    }
                    case "FriendFavourited": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        player.getFriendsList().getFriends().get(UUID.fromString(in.readUTF())).favourited(false);
                        break;
                    }
                    case "FriendUnfavourited": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        player.getFriendsList().getFriends().get(UUID.fromString(in.readUTF())).unfavourited(false);
                        break;
                    }
                    case "UpdateFriendsList": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        if (player != null) {
                            if (player.isLoaded()) {
                                for (Friend friend : player.getFriendsList().getFriends().values()) {
                                    if (friend.isOnline()) {
                                        friend.loggedOn(friend.getServer(), friend.getStatus(), true);
                                    }
                                }
                            }
                        }
                        break;
                    }
                    case "FriendTeleport": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        String server = in.readUTF();
                        ServerInfo target = ProxyAPI.getAmcServers().get(server);
                        if (target == null) {
                            player.sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("No results found for [**%s**]", server)));
                        } else {
                            if (!server.equalsIgnoreCase(player.getServer().getName())) {
                                switch (((String) target.getServerType().get("type")).toLowerCase()) {
                                    case "build":
                                        if (!player.hasPermission("build") && !player.hasPermission("admin")) {
                                            player.sendMessage(TextFormatter.pluginMessage("Server Manager", "You do not have permission to go to that server!"));
                                            return;
                                        }
                                        player.connect(target);
                                        player.sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("You are being sent from **%s** to **%s**.", player.getServer().getName(), target.getName())));
                                        break;
                                    case "staff":
                                        if (!player.hasPermission("moderation")) {
                                            player.sendMessage(TextFormatter.pluginMessage("Server Manager", "You do not have permission to go to that server!"));
                                            return;
                                        }
                                        player.connect(target);
                                        player.sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("You are being sent from **%s** to **%s**.", player.getServer().getName(), target.getName())));
                                        break;
                                    default:
                                        player.connect(target);
                                        player.sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("You are being sent from **%s** to **%s**.", player.getServer().getName(), target.getName())));
                                }
                            } else {
                                player.sendMessage(TextFormatter.pluginMessage("Server Manager", "You are already connected to that server!"));
                            }
                        }
                        break;
                    }
                    case "UpdateParty": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        if (player.getParty() != null) {
                            ByteArrayDataOutput out = ByteStreams.newDataOutput();
                            out.writeUTF("PartySet");
                            out.writeUTF(player.getName());
                            out.writeUTF(player.getParty().getUuid().toString());
                            player.sendPluginMessage(out.toByteArray());
                        } else {
                            ByteArrayDataOutput out = ByteStreams.newDataOutput();
                            out.writeUTF("PartySet");
                            out.writeUTF(player.getName());
                            out.writeUTF("null");
                            player.sendPluginMessage(out.toByteArray());
                        }
                        break;
                    }
                    case "ApprovalNotificationsChanged": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        boolean enabled = in.readBoolean();
                        player.getPreferences().setApprovalNotifications(enabled, false);
                        break;
                    }
                    case "ApprovalProcessedNotificationsChanged": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        boolean enabled = in.readBoolean();
                        player.getPreferences().setApprovalProcessedNotifications(enabled, false);
                        break;
                    }
                    case "ChatVisibilityChanged": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        boolean enabled = in.readBoolean();
                        player.getPreferences().setChatVisibility(enabled, false);
                        break;
                    }
                    case "FriendRequestsChanged": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        boolean enabled = in.readBoolean();
                        player.getPreferences().setFriendRequests(enabled, false);
                        break;
                    }
                    case "PreferredPronounsChanged": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        Pronoun pronouns = Pronoun.valueOf(in.readUTF());
                        player.getPreferences().setPreferredPronouns(pronouns, false);
                        break;
                    }
                    case "HideDisguiseNameChanged": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        boolean enabled = in.readBoolean();
                        player.getPreferences().setHideDisguiseName(enabled, false);
                        break;
                    }
                    case "HubFlightChanged": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        boolean enabled = in.readBoolean();
                        player.getPreferences().setHubFlight(enabled, false);
                        break;
                    }
                    case "HubForcefieldChanged": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        boolean enabled = in.readBoolean();
                        player.getPreferences().setHubForcefield(enabled, false);
                        break;
                    }
                    case "HubInvisibilityChanged": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        boolean enabled = in.readBoolean();
                        player.getPreferences().setHubInvisibility(enabled, false);
                        break;
                    }
                    case "HubSpeedChanged": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        boolean enabled = in.readBoolean();
                        player.getPreferences().setHubSpeed(enabled, false);
                        break;
                    }
                    case "HubVisibilityChanged": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        boolean enabled = in.readBoolean();
                        player.getPreferences().setHubVisibility(enabled, false);
                        break;
                    }
                    case "IgnoreHubKnockbackChanged": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        boolean enabled = in.readBoolean();
                        player.getPreferences().setIgnoreHubKnockback(enabled, false);
                        break;
                    }
                    case "MuteInformModeChanged": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        String mode = in.readUTF();
                        player.getPreferences().setMuteInformMode(PlayerPreferences.MuteInformMode.valueOf(mode), false);
                        break;
                    }
                    case "PartyRequestsChanged": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        boolean enabled = in.readBoolean();
                        player.getPreferences().setPartyRequests(enabled, false);
                        break;
                    }
                    case "PingOnPartyChatChanged": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        boolean enabled = in.readBoolean();
                        player.getPreferences().setPingOnPartyChat(enabled, false);
                        break;
                    }
                    case "PingOnPrivateMessageChanged": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        boolean enabled = in.readBoolean();
                        player.getPreferences().setPingOnPrivateMessage(enabled, false);
                        break;
                    }
                    case "PingOnChatMentionChanged": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        boolean enabled = in.readBoolean();
                        player.getPreferences().setPingOnChatMention(enabled, false);
                        break;
                    }
                    case "PrivateMessageModeChanged": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        String mode = in.readUTF();
                        player.getPreferences().setPrivateMessageMode(PlayerPreferences.PrivateMessageMode.valueOf(mode), false);
                        break;
                    }
                    case "ReportNotificationsChanged": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        boolean enabled = in.readBoolean();
                        player.getPreferences().setReportNotifications(enabled, false);
                        break;
                    }
                    case "SocialMediaNotificationsChanged": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        boolean enabled = in.readBoolean();
                        player.getPreferences().setSocialMediaNotifications(enabled, false);
                        break;
                    }
                    case "StaffLoginNotificationsChanged": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(in.readUTF()));
                        boolean enabled = in.readBoolean();
                        player.getPreferences().setStaffLoginNotifications(enabled, false);
                        break;
                    }
                    case "ChatReportSent": {
                        int reportId = in.readInt();
                        ProxyAPI.getCore().getProxy().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                            PlayerReport report = AuroraMCAPI.getDbManager().getReport(reportId);
                            AuroraMCProxyPlayer player = ProxyAPI.getPlayer(report.getReporters().get(0));
                            assert player != null;
                            UUID uuid;
                            if (report.getChatType() == PlayerReport.ChatType.PRIVATE) {
                                 uuid = ChatLogs.reportDump(report.getSuspect(), report.getReporters().get(0), null);
                            } else {
                                uuid = ChatLogs.reportDump(-1, -1, player.getParty().getUuid());
                            }

                            report.attachChatLog(uuid);
                        });
                        break;
                    }
                    case "ChatReportAppend": {
                        int reportId = in.readInt();
                        ProxyAPI.getCore().getProxy().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                            PlayerReport report = AuroraMCAPI.getDbManager().getReport(reportId);
                            ChatLogs.appendMessages(report.getChatReportUUID());
                        });
                        break;
                    }
                    case "ReportHandled": {
                        int reportId = in.readInt();
                        ProxyAPI.getCore().getProxy().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                            PlayerReport report = AuroraMCAPI.getDbManager().getReport(reportId);
                            if (report.getType() == PlayerReport.ReportType.INAPPROPRIATE_NAME && report.getOutcome() == PlayerReport.ReportOutcome.ACCEPTED) {
                                AuroraMCAPI.getDbManager().addUsernameBan(report.getSuspectName());
                                AuroraMCProxyPlayer player = ProxyAPI.getPlayer(report.getSuspect());
                                if (player != null) {
                                    player.disconnect(TextFormatter.pluginMessage("Username Manager", "This username is blacklisted.\n" +
                                            "\n" +
                                            "This username has been deemed inappropriate and is therefore\n" +
                                            "blacklisted from use on our network!\n\n" +
                                            "In order to join, simply change your name!"));
                                } else {
                                    UUID uuid = AuroraMCAPI.getDbManager().getUUID(report.getSuspect());
                                    if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                                        ProtocolMessage message = new ProtocolMessage(Protocol.PUNISH, AuroraMCAPI.getDbManager().getProxy(uuid).toString(), "blacklist", AuroraMCAPI.getInfo().getName(), uuid.toString());
                                        CommunicationUtils.sendMessage(message);
                                    }
                                }
                            }
                            BaseComponent message = TextFormatter.pluginMessage("Reports", String.format("Your report against **%s** has been §%s§r. Thank you for submitting a report!", report.getSuspectName(), ((report.getOutcome() == PlayerReport.ReportOutcome.ACCEPTED)?"aaccepted":"cdenied§r. After review, it was deemed that there was not enough evidence to punish the user at this time")));
                            List<UUID> uuidsToSend = new ArrayList<>();
                            for (int i : report.getReporters()) {
                                AuroraMCProxyPlayer player = ProxyAPI.getPlayer(i);
                                if (player != null) {
                                    player.sendMessage(message);
                                } else {
                                    UUID uuid = AuroraMCAPI.getDbManager().getUUID(i);
                                    if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                                        UUID proxy = AuroraMCAPI.getDbManager().getProxy(uuid);
                                        if (!uuidsToSend.contains(proxy) && !proxy.equals(UUID.fromString(AuroraMCAPI.getInfo().getName()))) {
                                            uuidsToSend.add(proxy);
                                        }
                                    } else {
                                        AuroraMCAPI.getDbManager().addOfflinereport(i);
                                    }
                                }
                            }
                            for (UUID uuid : uuidsToSend) {
                                ProtocolMessage message1 = new ProtocolMessage(Protocol.REPORT_NOTIFICATION, uuid.toString(), "", AuroraMCAPI.getInfo().getName(), reportId + "");
                                CommunicationUtils.sendMessage(message1);
                            }
                        });
                        break;
                    }
                    case "IgnoreAdded": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(in.readUTF());
                        int id = in.readInt();
                        String name = in.readUTF();
                        assert player != null;
                        player.addIgnored(new IgnoredPlayer(id, name), false);
                    }
                    case "IgnoreRemoved": {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(in.readUTF());
                        int id = in.readInt();
                        assert player != null;
                        player.addIgnored(player.getIgnored(id), false);
                    }
                    case "CosmeticAdd": {
                        UUID uuid = UUID.fromString(in.readUTF());
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(uuid);
                        if (player != null) {
                            if (player.isLoaded()) {
                                Cosmetic cosmetic = AuroraMCAPI.getCosmetics().get(in.readInt());
                                player.getUnlockedCosmetics().add(cosmetic);
                                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                out.writeUTF("CosmeticAdd");
                                out.writeUTF(player.getName());
                                out.writeInt(cosmetic.getId());
                                player.sendPluginMessage(out.toByteArray());
                            }
                        } else {
                            if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                                ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_PROFILE, AuroraMCAPI.getDbManager().getProxy(uuid).toString(), "add_cosmetic", AuroraMCAPI.getInfo().getName(), uuid.toString() + "\n" + in.readInt());
                                CommunicationUtils.sendMessage(message);
                            }
                        }
                        break;
                    }
                    case "CosmeticRemove": {
                        UUID uuid = UUID.fromString(in.readUTF());
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(uuid);
                        if (player != null) {
                            if (player.isLoaded()) {
                                Cosmetic cosmetic = AuroraMCAPI.getCosmetics().get(in.readInt());
                                player.getUnlockedCosmetics().remove(cosmetic);
                                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                out.writeUTF("CosmeticRemove");
                                out.writeUTF(player.getName());
                                out.writeInt(cosmetic.getId());
                                player.sendPluginMessage(out.toByteArray());
                            }
                        } else {
                            if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                                ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_PROFILE, AuroraMCAPI.getDbManager().getProxy(uuid).toString(), "remove_cosmetic", AuroraMCAPI.getInfo().getName(), uuid.toString() + "\n" + in.readInt());
                                CommunicationUtils.sendMessage(message);
                            }
                        }
                        break;
                    }
                    case "GlobalAccountSuspend": {
                        String code = in.readUTF();
                        List<Integer> ids = AuroraMCAPI.getDbManager().getGlobalAccountIDs(code);
                        String extraDetails = in.readUTF();
                        BaseComponent message = TextFormatter.pluginMessage("Punishments", String.format("" +
                                "You have been Globally Account Suspended.\n" +
                                "\n" +
                                "&rThe administration team has decided that due to your previous actions,\n" +
                                "&rwe are no longer going to allow your continued use of our services.\n" +
                                "\n" +
                                "&rThis type of punishment does not expire, and means you are no longer\n" +
                                "&rallowed to join the network, use the forums or communicate in Discord.\n" +
                                "\n" +
                                "&rAll logged IPs are now banned, and any account used to connect to the network\n" +
                                "&rthrough any logged IP will result in a permanent ban being issued.\n" +
                                "\n" +
                                "Reason: **%s**", extraDetails));
                        List<UUID> uuidsToSend = new ArrayList<>();
                        for (int id : ids) {
                            AuroraMCProxyPlayer player = ProxyAPI.getPlayer(id);
                            if (player != null) {
                                player.disconnect(message);
                            } else {
                                UUID uuid = AuroraMCAPI.getDbManager().getUUID(id);
                                if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                                    UUID proxy = AuroraMCAPI.getDbManager().getProxy(uuid);
                                    if (!uuidsToSend.contains(proxy) && !proxy.equals(UUID.fromString(AuroraMCAPI.getInfo().getName()))) {
                                        uuidsToSend.add(proxy);
                                    }
                                }
                            }
                        }

                        for (UUID uuid : uuidsToSend) {
                            ProtocolMessage message1 = new ProtocolMessage(Protocol.PUNISH, uuid.toString(), "global_account_suspension", AuroraMCAPI.getInfo().getName(), code + "\n" + extraDetails);
                            CommunicationUtils.sendMessage(message1);
                        }
                        break;
                    }
                    case "Lobby": {
                        UUID uuid = UUID.fromString(in.readUTF());
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(uuid);
                        List<ServerInfo> lobbies = ProxyAPI.getLobbyServers();
                        ServerInfo leastPopulated = null;
                        assert player != null;
                        for (ServerInfo lobby : lobbies) {
                            if ((leastPopulated == null || leastPopulated.getCurrentPlayers() > lobby.getCurrentPlayers()) && !player.getServer().getName().equals(lobby.getName())) {
                                leastPopulated = lobby;
                            }
                        }
                        assert leastPopulated != null;
                        player.sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("You are being sent from **%s** to **%s**.", player.getServer().getName(), leastPopulated.getName())));
                        player.connect(leastPopulated);
                        break;
                    }
                    case "SMP": {
                        UUID uuid = UUID.fromString(in.readUTF());
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(uuid);
                        assert player != null;
                        if (ProxyServer.getInstance().getPlayer(uuid).getPendingConnection().getVersion() != 763) {
                            player.sendMessage(TextFormatter.pluginMessage("NuttersSMP", "You must be in version **1.20.1** in order to connect to the §5§lNuttersSMP§r."));
                            return;
                        }
                        player.sendMessage(TextFormatter.pluginMessage("NuttersSMP", "Connecting you to §5§lNuttersSMP§r, please wait..."));
                        boolean isBlacklisted = AuroraMCAPI.getDbManager().isSMPBlacklist(player.getUniqueId().toString());
                        if (isBlacklisted) {
                            player.sendMessage(TextFormatter.pluginMessage("NuttersSMP", "§cYou are blacklisted from the NuttersSMP.\n\n§rIf you believe this to be a mistake, please contact @Heliology#3092 on Discord."));
                            return;
                        }

                        SMPLocation location = AuroraMCAPI.getDbManager().getSMPLogoutLocation(player.getUniqueId());
                        if (location != null) {
                            switch (location.getDimension()) {
                                case END: {
                                    player.sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("You are being sent from **%s** to **SMP-End**.", player.getServer().getName())));
                                    player.connect(ProxyAPI.getAmcServers().get("SMP-End"));
                                    break;
                                }
                                case NETHER: {
                                    player.sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("You are being sent from **%s** to **SMP-Nether**.", player.getServer().getName())));
                                    player.connect(ProxyAPI.getAmcServers().get("SMP-Nether"));
                                    break;
                                }
                                case OVERWORLD: {
                                    player.sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("You are being sent from **%s** to **SMP-Overworld**.", player.getServer().getName())));
                                    player.connect(ProxyAPI.getAmcServers().get("SMP-Overworld"));
                                    break;
                                }
                            }
                        } else {
                            player.sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("You are being sent from **%s** to **SMP-Overworld**.", player.getServer().getName())));
                            player.connect(ProxyAPI.getAmcServers().get("SMP-Overworld"));
                        }
                        break;
                    }

                    case "SMPOverworld": {
                        UUID uuid = UUID.fromString(in.readUTF());
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(uuid);
                        assert player != null;
                        player.sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("You are being sent from **%s** to **SMP-Overworld**.", player.getServer().getName())));
                        player.connect(ProxyAPI.getAmcServers().get("SMP-Overworld"));

                        break;
                    }
                    case "SMPNether": {
                        UUID uuid = UUID.fromString(in.readUTF());
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(uuid);
                        assert player != null;
                        player.sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("You are being sent from **%s** to **SMP-Nether**.", player.getServer().getName())));
                        player.connect(ProxyAPI.getAmcServers().get("SMP-Nether"));

                        break;
                    }
                    case "SMPEnd": {
                        UUID uuid = UUID.fromString(in.readUTF());
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(uuid);
                        assert player != null;
                        player.sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("You are being sent from **%s** to **SMP-End**.", player.getServer().getName())));
                        player.connect(ProxyAPI.getAmcServers().get("SMP-End"));

                        break;
                    }
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

}
