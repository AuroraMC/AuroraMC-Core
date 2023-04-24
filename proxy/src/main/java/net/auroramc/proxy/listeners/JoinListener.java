/*
 * Copyright (c) 2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.proxy.listeners;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.backend.info.ServerInfo;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.permissions.SubRank;
import net.auroramc.api.player.friends.Friend;
import net.auroramc.api.player.friends.FriendsList;
import net.auroramc.api.punishments.Ban;
import net.auroramc.api.punishments.GlobalAccountSuspension;
import net.auroramc.api.punishments.PunishmentLength;
import net.auroramc.api.punishments.Rule;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyAPI;
import net.auroramc.proxy.api.backend.communication.CommunicationUtils;
import net.auroramc.proxy.api.backend.communication.Protocol;
import net.auroramc.proxy.api.backend.communication.ProtocolMessage;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import net.auroramc.proxy.api.utils.LMCUtils;
import net.auroramc.proxy.api.utils.MaintenanceMode;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.protocol.packet.PluginMessage;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class JoinListener implements Listener {

    @EventHandler
    public void onPreLogin(LoginEvent e) {
        if (AuroraMCAPI.getDbManager().hasActiveSession(e.getConnection().getUniqueId())) {
            e.setCancelled(true);
            e.setCancelReason(TextFormatter.pluginMessage("Server Manager", "You are already logged in from another location!\n" +
                    "\n" +
                    "If you believe this is incorrect, please allow 5-10 minutes for your old session to expire before trying again.\n" +
                    "\n" +
                    "If you are still unable to connect, it is possible your account is compromised. In this event, **please contact a staff member immediately**."));
        }
        String ip = e.getConnection().getSocketAddress().toString().replace("/", "").split(":")[0];
        //First, check if the user has an active Global Account Suspension on the account itself.
        GlobalAccountSuspension suspension = AuroraMCAPI.getDbManager().getGlobalAccountSuspension(e.getConnection().getUniqueId());
        if (suspension != null) {
            e.setCancelled(true);
            e.setCancelReason(TextFormatter.pluginMessage("Punishments", String.format("" +
                    "You are Globally Account Suspended.\n" +
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
                    "Reason: **%s**", suspension.getReason())));

            //Check if this is a new IP, if so add it to the list of banned IP's.
            int id = AuroraMCAPI.getDbManager().getAuroraMCID(e.getConnection().getUniqueId());
            AuroraMCAPI.getDbManager().checkIP(ip, e.getConnection().getUniqueId(), suspension.getCode(), id);
            return;
        }

        //Now check if the IP has one active.
        suspension = AuroraMCAPI.getDbManager().getGlobalAccountSuspension(ip);
        if (suspension != null) {
            //It does, if it does not already have one issued, issue the ban.
            int id = AuroraMCAPI.getDbManager().getAuroraMCID(e.getConnection().getUniqueId());
            if (id == -1) {
                id = AuroraMCAPI.getDbManager().newUser(e.getConnection().getUniqueId(), e.getConnection().getName());
            }
            if (!AuroraMCAPI.getDbManager().isAlreadyGlobalBanned(id)) {
                String code = RandomStringUtils.randomAlphanumeric(8).toUpperCase();
                AuroraMCAPI.getDbManager().issuePunishment(code, id, 23, String.format("Joining through a Globally Banned IP address [%s]", suspension.getRootAccount()), 1, System.currentTimeMillis(), -1, 1, e.getConnection().getUniqueId().toString());
            }
            //Leave it for the normal ban checker to catch.
        }

        Ban ban = AuroraMCAPI.getDbManager().getBan(e.getConnection().getUniqueId().toString());
        if (ban != null) {
            PunishmentLength length = new PunishmentLength((ban.getExpire() - System.currentTimeMillis())/3600000d);
            Rule rule = AuroraMCAPI.getRules().getRule(ban.getRuleID());
            e.setCancelled(true);
            switch (ban.getStatus()) {
                case 2:
                    e.setCancelReason(TextFormatter.pluginMessage("Punishments",String.format("You have been banned from the network.\n" +
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
                            "&rIf you believe this was a false punishment, please appeal at appeal.auroramc.net.", rule.getRuleName(), ban.getExtraNotes(), length.getFormatted(), ban.getCode())));
                    break;
                case 3:
                    e.setCancelReason(TextFormatter.pluginMessage("Punishments",String.format("You have been banned from the network.\n" +
                            "\n" +
                            "&rReason: **%s - %s [SM Approved]**\n" +
                            "&rExpires:  **%s**\n" +
                            "\n" +
                            "&rPunishment Code: **%s**\n" +
                            "\n" +
                            "&rIf you believe this was a false punishment, please appeal at appeal.auroramc.net.", rule.getRuleName(), ban.getExtraNotes(), length.getFormatted(), ban.getCode())));
                    break;
                default:
                    e.setCancelReason(TextFormatter.pluginMessage("Punishments",String.format("You have been banned from the network.\n" +
                            "\n" +
                            "&rReason: **%s - %s**\n" +
                            "&rExpires:  **%s**\n" +
                            "\n" +
                            "&rPunishment Code: **%s**\n" +
                            "\n" +
                            "&rIf you believe this was a false punishment, please appeal at appeal.auroramc.net.", rule.getRuleName(), ban.getExtraNotes(), length.getFormatted(), ban.getCode())));
                    break;
            }
            return;
        }

        List<String> usernameBans = AuroraMCAPI.getDbManager().getUsernameBans();
        if (usernameBans.contains(e.getConnection().getName().toLowerCase())) {
            e.setCancelled(true);
            e.setCancelReason(TextFormatter.pluginMessage("Username Manager", "This username is blacklisted.\n" +
                    "\n" +
                    "This username has been deemed inappropriate and is therefore\n" +
                    "blacklisted from use on our network!\n\n" +
                    "In order to join, simply change your name!"));

        }

        if (ProxyAPI.getProxySettings().isMaintenance()) {
            int id = AuroraMCAPI.getDbManager().getAuroraMCID(e.getConnection().getUniqueId());
            if (id == -1) {
                AuroraMCAPI.getDbManager().newUser(e.getConnection().getUniqueId(), e.getConnection().getName());
            }
            Rank rank = AuroraMCAPI.getDbManager().getRank(e.getConnection().getUniqueId());
            List<SubRank> subRanks = AuroraMCAPI.getDbManager().getSubRanks(e.getConnection().getUniqueId());
            if (rank != null && subRanks != null) {
                boolean allowed = false;
                outer:
                for (Permission permission : ProxyAPI.getProxySettings().getMode().getAllowance()) {
                    if (rank.hasPermission(permission.getNode())) {
                        allowed = true;
                        break;
                    }
                    for (SubRank subrank : subRanks) {
                        if (subrank.hasPermission(permission.getNode())) {
                            allowed = true;
                            break outer;
                        }
                    }
                }

                if (!allowed) {
                    e.setCancelled(true);
                    if (ProxyAPI.getProxySettings().getMode() == MaintenanceMode.NOT_OPEN) {
                        e.setCancelReason(TextFormatter.pluginMessage("Maintenance", "AuroraMC is almost ready but not yet open.\n\n" +
                                "We are deep into preparations for our network opening and are almost\n" +
                                "ready for our public launch!\n" +
                                "\n" +
                                "Our launch date has been set: &b&l31st July 2022!&r You can join\n" +
                                "our discord right now via https://discord.auroramc.net!" +
                                "\n" +
                                "We do hope you will join us when we do open, and thanks for showing us\n" +
                                "how eager people are to join the network!\n" +
                                "**~The AuroraMC Network Leadership Team**"));
                    } else {
                        e.setCancelReason(TextFormatter.pluginMessage("Maintenance", "Uh oh! The network is in maintenance!\n\n" +
                                "In order to prevent issues, you will be unable to connect to\n" +
                                "the network while maintenance is in progress! Please feel free\n" +
                                "to keep an eye out on the forums as we will keep you updated on\n" +
                                "this maintenance and an ETA on when the network will be open again!\n\n" +
                                "We apologise for any inconveniences caused.\n" +
                                "**~The AuroraMC Network Leadership Team**"));
                    }
                }
            } else {
                e.setCancelled(true);
                if (ProxyAPI.getProxySettings().getMode() == MaintenanceMode.NOT_OPEN) {
                    e.setCancelReason(TextFormatter.pluginMessage("Maintenance", "AuroraMC is almost ready but not yet open.\n\n" +
                            "We are deep into preparations for our network opening and are almost\n" +
                            "ready for our public launch!\n" +
                            "\n" +
                            "Our launch date has been set: &b&l31st July 2022!&r You can join\n" +
                            "our discord right now via https://discord.auroramc.net!" +
                            "\n" +
                            "We do hope you will join us when we do open, and thanks for showing us\n" +
                            "how eager people are to join the network!\n" +
                            "**~The AuroraMC Network Leadership Team**"));
                } else {
                    e.setCancelReason(TextFormatter.pluginMessage("Maintenance", "Uh oh! The network is in maintenance!\n\n" +
                            "In order to prevent issues, you will be unable to connect to\n" +
                            "the network while maintenance is in progress! Please feel free\n" +
                            "to keep an eye out on the forums as we will keep you updated on\n" +
                            "this maintenance and an ETA on when the network will be open again!\n\n" +
                            "We apologise for any inconveniences caused.\n" +
                            "**~The AuroraMC Network Leadership Team**"));
                }
            }
        }
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent e) {
        //TODO: load in player details. Start a new session.
        ProxyAPI.newPlayer(e.getPlayer(), new AuroraMCProxyPlayer(e.getPlayer()));
        e.getPlayer().unsafe().sendPacket(new PluginMessage("badlion:mods", ("" +
                "{" +
                    "\"Waypoints\":{\"" +
                        "disabled\":true" +
                    "}," +
                    "\"MiniMap\":{" +
                        "\"disabled\":true" +
                    "}," +
                    "\"Schematica\": {" +
                        "\"disabled\":true" +
                    "}," +
                    "\"ToggleSneak\":{" +
                        "\"disabled\":false," +
                        "\"extra_data\":{" +
                            "\"inventorySneak\":{" +
                                "\"disabled\":true" +
                            "}" +
                        "}" +
                    "}," +
                    "\"ToggleSprint\":{" +
                        "\"disabled\":false," +
                        "\"extra_data\":{" +
                            "\"flySpeed\":{" +
                                "\"disabled\":true" +
                            "}" +
                        "}" +
                    "}" +
                "}").getBytes(), false));
        e.getPlayer().unsafe().sendPacket(new PluginMessage("LMC", LMCUtils.getBytesToSend("minimap", "{\"allowed\":false}"), false));
    }

    @EventHandler
    public void onConnect(ServerConnectEvent e) {
        Server from = e.getPlayer().getServer();

        switch (e.getReason()) {
            case LOBBY_FALLBACK:
                //Do not need to add in a listener here for security, if they cannot connect to the fallback then they just end up disconnected anyway.
            case KICK_REDIRECT:
                //Same again here, will just disconnect.
            case SERVER_DOWN_REDIRECT:
                //Again again here.
            case JOIN_PROXY:
                //connect them to a random hub.
                List<ServerInfo> lobbies = ProxyAPI.getLobbyServers();
                ServerInfo leastPopulated = null;
                for (ServerInfo lobby : lobbies) {
                    if (leastPopulated == null || leastPopulated.getCurrentPlayers() > lobby.getCurrentPlayers()) {
                        leastPopulated = lobby;
                    }
                }
                assert leastPopulated != null;
                net.md_5.bungee.api.config.ServerInfo info = ProxyAPI.getServers().get(leastPopulated.getName());
                e.setTarget(info);
                ProxyAPI.getCore().getLogger().info("attempting to server " + info.getName() + " IP: " + info.getAddress().toString());
                break;
            default:
                /* See if they have permission join the server, if not then cancel the event. If they do, continue it.
                   Types of restricted servers:
                    - Staff Servers
                    - Build Servers
                 */
                ServerInfo serverInfo = ProxyAPI.getAmcServers().get(e.getTarget().getName());
                if (serverInfo.getServerType().get("type") instanceof String) {
                    AuroraMCProxyPlayer player = ProxyAPI.getPlayer(e.getPlayer());
                    switch (((String) serverInfo.getServerType().get("type")).toLowerCase()) {
                        case "build":
                            if (!player.hasPermission("build") && !player.hasPermission("admin") && !player.hasPermission("events")) {
                                if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(7))) {
                                    player.getStats().achievementGained(AuroraMCAPI.getAchievement(7), 1, true);
                                }
                                e.setCancelled(true);
                                e.getPlayer().sendMessage(TextFormatter.pluginMessage("Server Manager", "You do not have permission to go to that server!"));
                            }
                            break;
                        case "staff":
                            if (!player.hasPermission("moderation")) {
                                if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(7))) {
                                    player.getStats().achievementGained(AuroraMCAPI.getAchievement(7), 1, true);
                                }
                                e.setCancelled(true);
                                e.getPlayer().sendMessage(TextFormatter.pluginMessage("Server Manager", "You do not have permission to go to that server!"));
                            }
                            break;
                    }
                }

        }

        int timeout = e.getRequest().getConnectTimeout();
        ProxyAPI.getCore().getProxy().getScheduler().schedule(ProxyAPI.getCore(), () -> {
            //Confirm they were actually sent to the server, otherwise set the value to what it was to avoid security issues.
            if (from != null) {
                if (e.getPlayer().getServer().getInfo().getName().equalsIgnoreCase(from.getInfo().getName())) {
                    ProxyAPI.getPlayer(e.getPlayer()).updateServer(ProxyAPI.getAmcServers().get(from.getInfo().getName()));
                }
            }
        }, timeout, TimeUnit.MILLISECONDS);

        ProxyAPI.getPlayer(e.getPlayer()).updateServer(ProxyAPI.getAmcServers().get(e.getTarget().getName()));
    }

    @EventHandler
    public void onConnection(ServerConnectedEvent e) {
        if (ProxyAPI.getPlayer(e.getPlayer()) != null) {
            if (ProxyAPI.getPlayer(e.getPlayer()).isLoaded()) {
                FriendsList.VisibilityMode visibilityMode = ProxyAPI.getPlayer(e.getPlayer()).getFriendsList().getVisibilityMode();
                for (Friend friend : ProxyAPI.getPlayer(e.getPlayer()).getFriendsList().getFriends().values()) {

                    ProxiedPlayer ppTarget = ProxyServer.getInstance().getPlayer(friend.getUuid());
                    if (ppTarget != null) {
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ppTarget);
                        if (!player.getFriendsList().getFriends().get(e.getPlayer().getUniqueId()).isOnline()) {
                            player.getFriendsList().getFriends().get(e.getPlayer().getUniqueId()).loggedOn(((visibilityMode != FriendsList.VisibilityMode.NOBODY) ? ((visibilityMode == FriendsList.VisibilityMode.FAVOURITE_FRIENDS_ONLY) ? ((friend.getType() == Friend.FriendType.FAVOURITE) ? e.getServer().getInfo().getName() : null) : e.getServer().getInfo().getName()) : null), ProxyAPI.getPlayer(e.getPlayer()).getFriendsList().getCurrentStatus(), true);
                        } else {
                            player.getFriendsList().getFriends().get(e.getPlayer().getUniqueId()).updateServer(((visibilityMode != FriendsList.VisibilityMode.NOBODY) ? ((visibilityMode == FriendsList.VisibilityMode.FAVOURITE_FRIENDS_ONLY) ? ((friend.getType() == Friend.FriendType.FAVOURITE) ? e.getServer().getInfo().getName() : null) : e.getServer().getInfo().getName()) : null), true);
                        }
                    } else {
                        ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                            if (AuroraMCAPI.getDbManager().hasActiveSession(friend.getUuid())) {
                                UUID proxy = AuroraMCAPI.getDbManager().getProxy(friend.getUuid());
                                ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_FRIENDS, proxy.toString(), "serverchange", e.getPlayer().getUniqueId().toString(), friend.getUuid().toString());
                                CommunicationUtils.sendMessage(message);
                            }
                        });
                    }
                }
            }
        }

    }

}
