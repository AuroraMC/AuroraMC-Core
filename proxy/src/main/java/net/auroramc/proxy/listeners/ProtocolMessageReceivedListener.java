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
import net.auroramc.api.player.Disguise;
import net.auroramc.api.player.PlayerReport;
import net.auroramc.api.player.friends.FriendsList;
import net.auroramc.api.punishments.Punishment;
import net.auroramc.api.punishments.PunishmentLength;
import net.auroramc.api.punishments.Rule;
import net.auroramc.api.stats.Achievement;
import net.auroramc.api.stats.TieredAcheivement;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyAPI;
import net.auroramc.proxy.api.backend.ProxyDatabaseManager;
import net.auroramc.proxy.api.backend.communication.CommunicationUtils;
import net.auroramc.proxy.api.backend.communication.Protocol;
import net.auroramc.proxy.api.backend.communication.ProtocolMessage;
import net.auroramc.proxy.api.events.ProtocolMessageEvent;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import net.auroramc.proxy.api.player.party.Party;
import net.auroramc.proxy.api.player.party.PartyInvite;
import net.auroramc.proxy.api.player.party.PartyPlayer;
import net.auroramc.proxy.api.utils.MaintenanceMode;
import net.auroramc.proxy.api.utils.PanelAccountType;
import net.auroramc.proxy.api.utils.targetselector.SelectorParseFailException;
import net.auroramc.proxy.api.utils.targetselector.TargetSelection;
import net.auroramc.proxy.api.utils.targetselector.TargetSelectorParser;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.chat.ComponentSerializer;
import net.md_5.bungee.event.EventHandler;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProtocolMessageReceivedListener implements Listener {

    @EventHandler
    public void onProtocolMessage(ProtocolMessageEvent e) {
        ProxyAPI.getCore().getLogger().info("Message received on protocol: " + e.getMessage().getProtocol().name() + ", command: " + e.getMessage().getCommand());
        switch (e.getMessage().getProtocol()) {
            case MESSAGE: {
                String message = e.getMessage().getExtraInfo();
                String to = e.getMessage().getCommand();
                String sender = e.getMessage().getSender();
                UUID uuid = AuroraMCAPI.getDbManager().getUUID(sender);
                if (sender.equalsIgnoreCase("Mission Control") && message.equals("store")) {
                    AuroraMCProxyPlayer player = ProxyAPI.getPlayer(UUID.fromString(to));
                    if (player != null) {
                        TextComponent component = new TextComponent("");

                        TextComponent lines = new TextComponent("--------------------------------------------");
                        lines.setStrikethrough(true);
                        lines.setColor(ChatColor.DARK_AQUA);
                        component.addExtra(lines);
                        component.addExtra("\n                      ");

                        TextComponent comp = new TextComponent("STORE PURCHASE");
                        comp.setColor(ChatColor.AQUA);
                        comp.setBold(true);
                        component.addExtra(comp);
                        component.addExtra("\n \n");

                        comp = new TextComponent("Your recent purchase has been credited to your account! You may need to logout and back into the network again to receive your full benefits!");
                        comp.setColor(ChatColor.WHITE);
                        comp.setBold(false);
                        component.addExtra(comp);

                        component.addExtra("\n \n");
                        component.addExtra(lines);
                        player.sendMessage(component);
                    }
                } else if (sender.equalsIgnoreCase("Mission Control")) {
                    BaseComponent bmessage = TextFormatter.pluginMessage("Mission Control", message);
                    AuroraMCProxyPlayer player = ProxyAPI.getPlayer(UUID.fromString(to));
                    if (player != null) {
                        player.sendMessage(bmessage);
                    }
                } else {
                    AuroraMCProxyPlayer player = ProxyAPI.getPlayer(UUID.fromString(to));
                    if (player != null) {
                        Disguise disguise = AuroraMCAPI.getDbManager().getDisguise(uuid);
                        if (disguise != null) {
                            sender = disguise.getName();
                        }
                        String rawMessage = message;
                        BaseComponent bmessage = TextFormatter.privateMessage(sender, player, ComponentSerializer.parse(message)[0]);
                        player.sendMessage(bmessage);
                        Rank rank = AuroraMCAPI.getDbManager().getRank(uuid);
                        int id = AuroraMCAPI.getDbManager().getAuroraMCID(uuid);
                        ChatLogs.chatMessage(id, sender, rank, rawMessage, false, player.getChannel(), player.getId(), player.getName(), null);
                        if (player.getPreferences().isPingOnPrivateMessageEnabled()) {
                            ByteArrayDataOutput out = ByteStreams.newDataOutput();
                            out.writeUTF("PlaySound");
                            out.writeUTF(player.getName());
                            out.writeUTF("NOTE_PLING");
                            out.writeInt(100);
                            out.writeInt(2);
                            player.sendPluginMessage(out.toByteArray());
                        }
                        if (player.getActiveMutes().size() > 0) {
                            switch (player.getPreferences().getMuteInformMode()) {
                                case MESSAGE_AND_MENTIONS:
                                case MESSAGE_ONLY:
                                    BaseComponent msg = TextFormatter.privateMessage(player, sender, new TextComponent("Hey! I'm currently muted and cannot message you right now."));
                                    player.sendMessage(msg);
                                    ProtocolMessage protocolMessage = new ProtocolMessage(Protocol.MESSAGE, AuroraMCAPI.getDbManager().getProxy(uuid).toString(), uuid.toString(), player.getName(), "Hey! I'm currently muted and cannot message you right now.");
                                    CommunicationUtils.sendMessage(protocolMessage);
                            }
                        }
                    }
                }
                break;
            }
            case SHUTDOWN: {
                ProxyAPI.scheduleShutdown(e.getMessage().getCommand());
                break;
            }
            case UPDATE_MOTD: {
                if (e.getMessage().getCommand().equalsIgnoreCase("maintenance")) {
                    ProxyAPI.getProxySettings().setMaintenanceMotd(e.getMessage().getExtraInfo());
                } else {
                    ProxyAPI.getProxySettings().setMotd(e.getMessage().getExtraInfo());
                }
                break;
            }
            case GLOBAL_MESSAGE: {
                String name = e.getMessage().getSender();
                Rank rank = Rank.valueOf(e.getMessage().getCommand());
                for (AuroraMCProxyPlayer player : ProxyAPI.getPlayers()) {
                    player.sendMessage(TextFormatter.chatMessage(name, rank, ComponentSerializer.parse(e.getMessage().getExtraInfo())[0]));
                }
                break;
            }
            case ANNOUNCE: {
                String selectors = e.getMessage().getCommand();
                TargetSelection selection;
                try {
                    selection = TargetSelectorParser.parse(selectors);
                } catch (SelectorParseFailException exception) {
                    exception.printStackTrace();
                    return;
                }
                TextComponent message = (TextComponent) TextComponent.fromLegacyText(e.getMessage().getExtraInfo())[0];
                Title title = ProxyServer.getInstance().createTitle();
                title.fadeIn(10);
                title.stay(100);
                title.fadeOut(10);
                TextComponent component = new TextComponent("ANNOUNCEMENT");
                component.setColor(ChatColor.DARK_AQUA);
                component.setBold(true);
                title.title(component);
                title.subTitle(message);
                for (AuroraMCProxyPlayer x : ProxyAPI.getPlayers()) {
                    if (selection.shouldSend(x)) {
                        x.sendTitle(title);
                        x.sendMessage(TextFormatter.pluginMessage("Announcement", message));
                    }
                }
                break;
            }
            case UPDATE_RULES: {
                AuroraMCAPI.loadRules();
                break;
            }
            case UPDATE_FRIENDS: {
                UUID sender = UUID.fromString(e.getMessage().getSender());
                UUID target = UUID.fromString(e.getMessage().getExtraInfo());
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(target);
                AuroraMCProxyPlayer AuroraMCProxyPlayer = ProxyAPI.getPlayer(player);
                if (player != null) {
                    switch (e.getMessage().getCommand().toLowerCase()) {
                        //An outgoing friend request was accepted.
                        case "accepted": {
                                FriendsList.VisibilityMode visibilityMode = AuroraMCAPI.getDbManager().getVisibilityMode(sender);
                                FriendStatus status = AuroraMCAPI.getDbManager().getFriendStatus(sender);
                                String server = AuroraMCAPI.getDbManager().getCurrentServer(sender);
                                String name = AuroraMCAPI.getDbManager().getNameFromUUID(sender.toString());
                                if (!AuroraMCProxyPlayer.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(30))) {
                                    AuroraMCProxyPlayer.getStats().achievementGained(AuroraMCAPI.getAchievement(30), 1, true);
                                }
                                AuroraMCProxyPlayer.getFriendsList().friendRequestAccepted(sender, true, ((visibilityMode != FriendsList.VisibilityMode.NOBODY)?((visibilityMode == FriendsList.VisibilityMode.FAVOURITE_FRIENDS_ONLY)?null:server):null), status, true);
                                player.sendMessage(TextFormatter.pluginMessage("Friends", String.format("Your friend request to **%s** was accepted!", name)));
                                break;
                        }
                        //A new incoming friend request.
                        case "incoming": {
                                String name = AuroraMCAPI.getDbManager().getNameFromUUID(sender.toString());
                                int id = AuroraMCAPI.getDbManager().getAuroraMCID(sender);
                                AuroraMCProxyPlayer.getFriendsList().newFriendRequest(sender, name, id, false, false);
                                TextComponent textComponent = new TextComponent("");
                                TextComponent lines = new TextComponent("▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆");
                                lines.setBold(true);
                                lines.setColor(ChatColor.DARK_AQUA);

                                textComponent.addExtra(lines);
                                textComponent.addExtra("\n\n");
                                textComponent.addExtra(TextFormatter.highlight(String.format("Incoming Friend Request from **%s**", name)));
                                textComponent.addExtra("\n\n");

                                TextComponent accept = new TextComponent("ACCEPT");
                                accept.setColor(ChatColor.GREEN);
                                accept.setBold(true);

                                accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to accept the friend request!").color(ChatColor.GREEN).create()));
                                accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/friend accept %s", name)));

                                textComponent.addExtra(accept);
                                textComponent.addExtra(" ");

                                TextComponent deny = new TextComponent("DENY");
                                deny.setColor(ChatColor.RED);
                                deny.setBold(true);

                                deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to deny the friend request!").color(ChatColor.RED).create()));
                                deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/friend deny %s", name)));

                                textComponent.addExtra(deny);
                                textComponent.addExtra("\n\n");
                                textComponent.addExtra(lines);
                                AuroraMCProxyPlayer.sendMessage(textComponent);
                                break;
                        }
                        //A friend request has been cancelled.
                        case "cancel": {
                            AuroraMCProxyPlayer.getFriendsList().friendRequestRemoved(sender, true, false);
                            break;
                        }
                        case "remove": {
                            AuroraMCProxyPlayer.getFriendsList().friendRemoved(sender, true, false);
                            break;
                        }
                        case "serverchange":
                        case "visibility": {
                            FriendsList.VisibilityMode visibilityMode = AuroraMCAPI.getDbManager().getVisibilityMode(sender);
                            int i = AuroraMCAPI.getDbManager().getAuroraMCID(sender);
                            boolean isFavourite = AuroraMCAPI.getDbManager().isFavouriteFriend(AuroraMCProxyPlayer.getId(), i);
                            String server = AuroraMCAPI.getDbManager().getCurrentServer(sender);
                            AuroraMCProxyPlayer.getFriendsList().getFriends().get(sender).updateServer(((visibilityMode != FriendsList.VisibilityMode.NOBODY)?((visibilityMode == FriendsList.VisibilityMode.FAVOURITE_FRIENDS_ONLY)?((isFavourite)?server:null):server):null), true);
                            break;
                        }
                        case "status": {
                            FriendStatus status = AuroraMCAPI.getDbManager().getFriendStatus(sender);
                            AuroraMCProxyPlayer.getFriendsList().getFriends().get(sender).setStatus(status, false);
                            break;
                        }
                        case "loggedoff": {
                            AuroraMCProxyPlayer.getFriendsList().getFriends().get(sender).loggedOff(false);
                            break;
                        }
                        case "loggedon": {
                            FriendsList.VisibilityMode visibilityMode = AuroraMCAPI.getDbManager().getVisibilityMode(sender);
                            int i = AuroraMCAPI.getDbManager().getAuroraMCID(sender);
                            boolean isFavourite = AuroraMCAPI.getDbManager().isFavouriteFriend(AuroraMCProxyPlayer.getId(), i);
                            String server = AuroraMCAPI.getDbManager().getCurrentServer(sender);
                            FriendStatus status = AuroraMCAPI.getDbManager().getFriendStatus(sender);
                            AuroraMCProxyPlayer.getFriendsList().getFriends().get(sender).loggedOn(((visibilityMode != FriendsList.VisibilityMode.NOBODY)?((visibilityMode == FriendsList.VisibilityMode.FAVOURITE_FRIENDS_ONLY)?((isFavourite)?server:null):server):null),status, true);
                            break;
                        }
                    }
                }
                break;
            }
            case UPDATE_CHAT_SLOW: {
                short i = Short.parseShort(e.getMessage().getExtraInfo());
                ProxyAPI.setChatSlow(i);
                break;
            }
            case EMERGENCY_SHUTDOWN: {
                ProxyAPI.shutdownNow(e.getMessage().getCommand());
                break;
            }
            case UPDATE_CHAT_SILENCE: {
                switch (e.getMessage().getCommand().toLowerCase()) {
                    case "enable": {
                        short amount = Short.parseShort(e.getMessage().getExtraInfo());
                        ProxyAPI.enableChatSilence(amount, true);
                        break;
                    }
                    case "disable": {
                        ProxyAPI.disableSilence();
                        break;
                    }
                }
                break;
            }
            case UPDATE_PLAYER_COUNT: {
                int amount = ProxyServer.getInstance().getPlayers().size();
                ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_PLAYER_COUNT, "Mission Control", "reply", AuroraMCAPI.getInfo().getName(), amount + "\n" + AuroraMCAPI.getInfo().getNetwork().name());
                CommunicationUtils.sendMessage(message);
                break;
            }
            case UPDATE_MAINTENANCE_MODE: {
                switch (e.getMessage().getCommand().toLowerCase()) {
                    case "enable": {
                        ProxyAPI.getProxySettings().enableMaintenance(MaintenanceMode.valueOf(e.getMessage().getExtraInfo()));
                        break;
                    }
                    case "disable": {
                        ProxyAPI.getProxySettings().disableMaintenance();
                        break;
                    }
                    case "update": {
                        ProxyAPI.getProxySettings().setMode(MaintenanceMode.valueOf(e.getMessage().getExtraInfo()));
                        break;
                    }
                }
                break;
            }
            case PARTY: {
                switch (e.getMessage().getCommand().toLowerCase()) {
                    case "new_invite": {
                        String[] args = e.getMessage().getExtraInfo().split("\n");
                        if (args.length == 2) {
                            UUID partyUUID = UUID.fromString(args[0]);
                            UUID inviteeUUID = UUID.fromString(args[1]);
                            PartyPlayer partyPlayer;
                            if (ProxyServer.getInstance().getPlayer(inviteeUUID) == null) {
                                String name = AuroraMCAPI.getDbManager().getNameFromUUID(args[1]);
                                Rank rank = AuroraMCAPI.getDbManager().getRank(inviteeUUID);
                                UUID proxyUUID = AuroraMCAPI.getDbManager().getProxy(inviteeUUID);
                                int id = AuroraMCAPI.getDbManager().getAuroraMCID(inviteeUUID);
                                partyPlayer = new PartyPlayer(id, name, inviteeUUID, null, proxyUUID, rank);
                            } else {
                                AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(inviteeUUID));
                                assert player != null;
                                if (player.getPartyPlayer() != null) {
                                    partyPlayer = player.getPartyPlayer();
                                } else {
                                    partyPlayer = new PartyPlayer(player.getId(), player.getName(), inviteeUUID, player, null, player.getRank());
                                }
                            }
                            Party party;
                            if (ProxyAPI.getParties().containsKey(partyUUID)) {
                                party = ProxyAPI.getParties().get(partyUUID);
                                PartyInvite invite = new PartyInvite(party, partyPlayer, ProxyDatabaseManager.getInviteExpire(partyUUID, inviteeUUID));
                                party.newInvite(invite);
                            } else {
                                party = ProxyDatabaseManager.getParty(partyUUID);
                                PartyInvite remove = null;
                                for (PartyInvite invite : party.getPartyInvites()) {
                                    if (invite.getPlayer().getUuid().equals(inviteeUUID)) {
                                        remove = invite;
                                        break;
                                    }
                                }
                                if (remove != null) {
                                    party.removePartyInvite(remove);
                                }
                                PartyInvite invite = new PartyInvite(party, partyPlayer, ProxyDatabaseManager.getInviteExpire(partyUUID, inviteeUUID));
                                party.newInvite(invite);
                            }
                        }
                        //Do nothing if the args doesn't match expected values.
                        break;
                    }
                    case "invite_accepted": {
                        String[] args = e.getMessage().getExtraInfo().split("\n");
                        if (args.length == 2) {
                            UUID partyUUID = UUID.fromString(args[0]);
                            UUID inviteeUUID = UUID.fromString(args[1]);
                            PartyPlayer partyPlayer;
                            if (ProxyServer.getInstance().getPlayer(inviteeUUID) == null) {
                                String name = AuroraMCAPI.getDbManager().getNameFromUUID(args[1]);
                                Rank rank = AuroraMCAPI.getDbManager().getRank(inviteeUUID);
                                UUID proxyUUID = AuroraMCAPI.getDbManager().getProxy(inviteeUUID);
                                int id = AuroraMCAPI.getDbManager().getAuroraMCID(inviteeUUID);
                                partyPlayer = new PartyPlayer(id, name, inviteeUUID, null, proxyUUID, rank);
                            } else {
                                AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(inviteeUUID));
                                assert player != null;
                                if (player.getPartyPlayer() != null) {
                                    partyPlayer = player.getPartyPlayer();
                                } else {
                                    partyPlayer = new PartyPlayer(player.getId(), player.getName(), inviteeUUID, player, null, player.getRank());
                                }
                            }
                            Party party = ProxyAPI.getParties().get(partyUUID);
                            party.requestAccepted(partyPlayer);
                        }
                        break;
                    }
                    case "invite_cancelled": {
                        String[] args = e.getMessage().getExtraInfo().split("\n");
                        if (args.length == 2) {
                            UUID partyUUID = UUID.fromString(args[0]);
                            UUID inviteeUUID = UUID.fromString(args[1]);
                            PartyPlayer partyPlayer;
                            if (ProxyServer.getInstance().getPlayer(inviteeUUID) == null) {
                                String name = AuroraMCAPI.getDbManager().getNameFromUUID(args[1]);
                                Rank rank = AuroraMCAPI.getDbManager().getRank(inviteeUUID);
                                UUID proxyUUID = AuroraMCAPI.getDbManager().getProxy(inviteeUUID);
                                int id = AuroraMCAPI.getDbManager().getAuroraMCID(inviteeUUID);
                                partyPlayer = new PartyPlayer(id, name, inviteeUUID, null, proxyUUID, rank);
                            } else {
                                AuroraMCProxyPlayer player = ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(inviteeUUID));
                                assert player != null;
                                if (player.getPartyPlayer() != null) {
                                    partyPlayer = player.getPartyPlayer();
                                } else {
                                    partyPlayer = new PartyPlayer(player.getId(), player.getName(), inviteeUUID, player, null, player.getRank());
                                }
                            }
                            Party party = ProxyAPI.getParties().get(partyUUID);
                            party.requestDenied(partyPlayer);
                        }
                        break;
                    }
                    case "disband_left": {
                        UUID uuid = UUID.fromString(e.getMessage().getExtraInfo());
                        Party party = ProxyAPI.getParties().get(uuid);
                        if (party != null) {
                            party.disbandLeft();
                        }
                        break;
                    }
                    case "disband": {
                        UUID uuid = UUID.fromString(e.getMessage().getExtraInfo());
                        Party party = ProxyAPI.getParties().get(uuid);
                        if (party != null) {
                            party.disband();
                        }
                        break;
                    }
                    case "transfer": {
                        UUID uuid = UUID.fromString(e.getMessage().getExtraInfo());
                        Party party = ProxyAPI.getParties().get(uuid);
                        if (party != null) {
                            UUID newLeader = ProxyDatabaseManager.getPartyLeader(party.getUuid());
                            PartyPlayer partyPlayer = null;
                            for (PartyPlayer player : party.getPlayers()) {
                                if (player.getUuid().equals(newLeader)) {
                                    partyPlayer = player;
                                    break;
                                }
                            }
                            if (partyPlayer != null) {
                                party.transfer(partyPlayer);
                            }
                        }
                        break;
                    }
                    case "left": {
                        String[] args = e.getMessage().getExtraInfo().split("\n");
                        if (args.length == 2) {
                            Party party = ProxyAPI.getParties().get(UUID.fromString(args[0]));
                            UUID left = UUID.fromString(args[1]);
                            PartyPlayer partyPlayer = null;
                            for (PartyPlayer player : party.getPlayers()) {
                                if (player.getUuid().equals(left)) {
                                    partyPlayer = player;
                                    break;
                                }
                            }
                            party.leave(partyPlayer, true);
                        }
                        break;
                    }
                    case "remove": {
                        String[] args = e.getMessage().getExtraInfo().split("\n");
                        if (args.length == 2) {
                            Party party = ProxyAPI.getParties().get(UUID.fromString(args[0]));
                            UUID left = UUID.fromString(args[1]);
                            PartyPlayer partyPlayer = null;
                            for (PartyPlayer player : party.getPlayers()) {
                                if (player.getUuid().equals(left)) {
                                    partyPlayer = player;
                                    break;
                                }
                            }
                            party.remove(partyPlayer, true);
                        }
                        break;
                    }
                    case "message": {
                        String[] args = e.getMessage().getExtraInfo().split("\n");
                        if (args.length == 4) {
                            Party party = ProxyAPI.getParties().get(UUID.fromString(args[0]));
                            UUID sender = UUID.fromString(args[1]);
                            String message = args[2];
                            String filtered = args[3];
                            if (party.getLeader().getUuid().equals(sender)) {
                                party.partyChat(party.getLeader(), ComponentSerializer.parse(message)[0], filtered);
                                return;
                            }
                            for (PartyPlayer player : party.getPlayers()) {
                                if (player.getUuid().equals(sender)) {
                                    party.partyChat(player, ComponentSerializer.parse(message)[0], filtered);
                                    return;
                                }
                            }
                        }
                        break;
                    }
                    case "warp": {
                        String[] args = e.getMessage().getExtraInfo().split("\n");
                        if (args.length == 2) {
                            Party party = ProxyAPI.getParties().get(UUID.fromString(args[0]));
                            String server = args[1];
                            ServerInfo info = ProxyAPI.getAmcServers().get(server);
                            if (info != null) {
                                party.warp(info);
                            }
                        }
                        break;
                    }
                }
                break;
            }
            case PUNISH: {
                switch (e.getMessage().getCommand().toLowerCase()) {
                    case "global_account_suspension": {
                        String[] args = e.getMessage().getExtraInfo().split("\n");
                        if (args.length == 2) {
                            String code = args[0];
                            List<Integer> ids = AuroraMCAPI.getDbManager().getGlobalAccountIDs(code);
                            String extraDetails = args[1];
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
                            for (int id : ids) {
                                AuroraMCProxyPlayer player = ProxyAPI.getPlayer(id);
                                if (player != null) {
                                    player.disconnect(message);
                                }
                            }
                        }
                        break;
                    }
                    case "blacklist": {
                        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(UUID.fromString(e.getMessage().getExtraInfo()));
                        player.disconnect(TextFormatter.pluginMessage("Username Manager", "This username is blacklisted.\n" +
                                "\n" +
                                "This username has been deemed inappropriate and is therefore\n" +
                                "blacklisted from use on our network!\n\n" +
                                "In order to join, simply change your name!"));
                        break;
                    }
                    case "smpblacklist": {
                        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(UUID.fromString(e.getMessage().getExtraInfo()));
                        AuroraMCProxyPlayer proxyPlayer = ProxyAPI.getPlayer(player);
                        if (proxyPlayer.getServer().getName().startsWith("SMP")) {
                            proxyPlayer.sendMessage(TextFormatter.pluginMessage("NuttersSMP", "§cYou have been blacklisted from the NuttersSMP.§r You are being sent to a Lobby.\n\nIf you believe this to be a mistake, please contact @Heliology#3092 on Discord."));
                            List<ServerInfo> lobbies = ProxyAPI.getLobbyServers();
                            ServerInfo leastPopulated = null;
                            for (ServerInfo lobby : lobbies) {
                                if ((leastPopulated == null || leastPopulated.getCurrentPlayers() > lobby.getCurrentPlayers()) && !proxyPlayer.getServer().getName().equals(lobby.getName())) {
                                    leastPopulated = lobby;
                                }
                            }
                            assert leastPopulated != null;
                            proxyPlayer.sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("You are being sent from **%s** to **%s**.", proxyPlayer.getServer().getName(), leastPopulated.getName())));
                            proxyPlayer.connect(leastPopulated);
                        }
                        break;
                    }
                    case "ban": {
                        Punishment punishment = AuroraMCAPI.getDbManager().getPunishment(e.getMessage().getExtraInfo());
                        PunishmentLength length = new PunishmentLength((punishment.getExpire() - System.currentTimeMillis())/3600000d);
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(punishment.getPunished());
                        Rule rule = AuroraMCAPI.getRules().getRule(punishment.getRuleID());
                        assert player != null;
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
                        break;
                    }
                    case "mute": {
                        Punishment punishment = AuroraMCAPI.getDbManager().getPunishment(e.getMessage().getExtraInfo());
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(punishment.getPunished());
                        if (player != null) {
                            if (player.isOnline()) {
                                player.applyMute(punishment);
                            }
                        }
                        break;
                    }
                    case "unmute": {
                        Punishment punishment = AuroraMCAPI.getDbManager().getPunishment(e.getMessage().getExtraInfo());
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(punishment.getPunished());
                        assert player != null;
                        player.removeMute(punishment);
                        break;
                    }
                    case "warning": {
                        Punishment punishment = AuroraMCAPI.getDbManager().getPunishment(e.getMessage().getExtraInfo());
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(punishment.getPunished());
                        Rule rule = AuroraMCAPI.getRules().getRule(punishment.getRuleID());
                        assert player != null;
                        player.sendMessage(TextFormatter.pluginMessage("Punishments",String.format("You have been issued a warning.\n" +
                                "Reason: **%s - %s**\n" +
                                "Punishment Code: **%s**", rule.getRuleName(), punishment.getExtraNotes(), punishment.getPunishmentCode())));
                        break;
                    }
                    case "approved": {
                        Punishment punishment = AuroraMCAPI.getDbManager().getPunishment(e.getMessage().getExtraInfo());
                        AuroraMCProxyPlayer player = ProxyAPI.getPlayer(punishment.getPunished());
                        assert player != null;
                        player.punishmentApproved(punishment);
                    }
                }
                break;
            }
            case UPDATE_PROFILE: {
                switch (e.getMessage().getCommand().toLowerCase()) {
                    case "addcrowns": {
                        String[] args = e.getMessage().getExtraInfo().split("\n");
                        if (args.length == 2) {
                            UUID uuid = UUID.fromString(args[0]);
                            long amount = Long.parseLong(args[1]);
                            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
                            if (target != null) {
                                AuroraMCProxyPlayer aTarget = ProxyAPI.getPlayer(target);
                                aTarget.getBank().addCrowns(amount, false, true);
                                target.sendMessage(TextFormatter.pluginMessage("Economy", String.format("You have received **%s Crowns** from an admin.", amount)));
                            }
                        }
                        break;
                    }
                    case "removecrowns": {
                        String[] args = e.getMessage().getExtraInfo().split("\n");
                        if (args.length == 2) {
                            UUID uuid = UUID.fromString(args[0]);
                            long amount = Long.parseLong(args[1]);
                            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
                            if (target != null) {
                                AuroraMCProxyPlayer aTarget = ProxyAPI.getPlayer(target);
                                aTarget.getBank().withdrawCrowns(amount, false, true);
                                target.sendMessage(TextFormatter.pluginMessage("Economy", String.format("**%s Crowns** have been removed from your account by an admin.", amount)));
                            }
                        }
                        break;
                    }
                    case "addtickets": {
                        String[] args = e.getMessage().getExtraInfo().split("\n");
                        if (args.length == 2) {
                            UUID uuid = UUID.fromString(args[0]);
                            long amount = Long.parseLong(args[1]);
                            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
                            if (target != null) {
                                AuroraMCProxyPlayer aTarget = ProxyAPI.getPlayer(target);
                                aTarget.getBank().addTickets(amount, false, true);
                                target.sendMessage(TextFormatter.pluginMessage("Economy", String.format("You have received **%s Tickets** from an admin.", amount)));
                            }
                        }
                        break;
                    }
                    case "removetickets": {
                        String[] args = e.getMessage().getExtraInfo().split("\n");
                        if (args.length == 2) {
                            UUID uuid = UUID.fromString(args[0]);
                            long amount = Long.parseLong(args[1]);
                            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
                            if (target != null) {
                                AuroraMCProxyPlayer aTarget = ProxyAPI.getPlayer(target);
                                aTarget.getBank().withdrawTickets(amount, false, true);
                                target.sendMessage(TextFormatter.pluginMessage("Economy", String.format("**%s Tickets** have been removed from your account by an admin.", amount)));
                            }
                        }
                        break;
                    }
                    case "add_achievement": {
                        String[] args = e.getMessage().getExtraInfo().split("\n");
                        if (args.length == 3) {
                            UUID uuid = UUID.fromString(args[0]);
                            Achievement acheivement = AuroraMCAPI.getAchievement(Integer.parseInt(args[1]));
                            int tier = Integer.parseInt(args[2]);
                            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
                            if (target != null) {
                                AuroraMCProxyPlayer aTarget = ProxyAPI.getPlayer(target);
                                aTarget.getStats().achievementGained(acheivement, tier, true);
                            }
                        }
                        break;
                    }
                    case "add_achievement_update": {
                        String[] args = e.getMessage().getExtraInfo().split("\n");
                        if (args.length == 3) {
                            UUID uuid = UUID.fromString(args[0]);
                            TieredAcheivement acheivement = (TieredAcheivement) AuroraMCAPI.getAchievement(Integer.parseInt(args[1]));
                            int tier = Integer.parseInt(args[2]);
                            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
                            if (target != null) {
                                AuroraMCProxyPlayer aTarget = ProxyAPI.getPlayer(target);
                                aTarget.getStats().achievementGained(acheivement, tier, true);
                                aTarget.getStats().setAchievementProgress(acheivement, acheivement.getTier(tier).getRequirement(), true);
                            }
                        }
                        break;
                    }
                    case "remove_achievement": {
                        String[] args = e.getMessage().getExtraInfo().split("\n");
                        if (args.length == 2) {
                            UUID uuid = UUID.fromString(args[0]);
                            Achievement acheivement = AuroraMCAPI.getAchievement(Integer.parseInt(args[1]));
                            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
                            if (target != null) {
                                AuroraMCProxyPlayer aTarget = ProxyAPI.getPlayer(target);
                                aTarget.getStats().achievementRemoved(acheivement, true);
                            }
                        }
                        break;
                    }
                    case "increment_statistic": {
                        String[] args = e.getMessage().getExtraInfo().split("\n");
                        if (args.length == 4) {
                            UUID uuid = UUID.fromString(args[0]);
                            int game = Integer.parseInt(args[1]);
                            String stat = args[2];
                            long amount = Long.parseLong(args[3]);
                            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
                            if (target != null) {
                                AuroraMCProxyPlayer aTarget = ProxyAPI.getPlayer(target);
                                aTarget.getStats().incrementStatistic(game, stat, amount, true);
                            }
                        }
                        break;
                    }
                    case "add_xp": {
                        String[] args = e.getMessage().getExtraInfo().split("\n");
                        if (args.length == 2) {
                            UUID uuid = UUID.fromString(args[0]);
                            long amount = Long.parseLong(args[1]);
                            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
                            if (target != null) {
                                AuroraMCProxyPlayer aTarget = ProxyAPI.getPlayer(target);
                                aTarget.getStats().addXp(amount, true);
                            }
                        }
                        break;
                    }
                    case "remove_xp": {
                        String[] args = e.getMessage().getExtraInfo().split("\n");
                        if (args.length == 2) {
                            UUID uuid = UUID.fromString(args[0]);
                            long amount = Long.parseLong(args[1]);
                            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
                            if (target != null) {
                                AuroraMCProxyPlayer aTarget = ProxyAPI.getPlayer(target);
                                aTarget.getStats().removeXp(amount, true);
                            }
                        }
                        break;
                    }
                    case "new_subscription": {
                        UUID uuid = UUID.fromString(e.getMessage().getExtraInfo());
                        ProxyAPI.getPlayer(ProxyServer.getInstance().getPlayer(uuid)).newSubscription();
                        break;
                    }
                    case "extend_subscription": {
                        String[] args = e.getMessage().getExtraInfo().split("\n");
                        if (args.length == 2) {
                            UUID uuid = UUID.fromString(args[0]);
                            int amount = Integer.parseInt(args[1]);
                            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
                            if (target != null) {
                                AuroraMCProxyPlayer aTarget = ProxyAPI.getPlayer(target);
                                aTarget.getActiveSubscription().extend(amount);
                            }
                        }
                        break;
                    }
                    case "add_cosmetic": {
                        String[] args = e.getMessage().getExtraInfo().split("\n");
                        if (args.length == 2) {
                            AuroraMCProxyPlayer player = ProxyAPI.getPlayer(UUID.fromString(args[0]));
                            assert player != null;
                            Cosmetic cosmetic = AuroraMCAPI.getCosmetics().get(Integer.parseInt(args[1]));
                            player.getUnlockedCosmetics().add(cosmetic);
                            ByteArrayDataOutput out = ByteStreams.newDataOutput();
                            out.writeUTF("CosmeticAdd");
                            out.writeUTF(player.getName());
                            out.writeInt(cosmetic.getId());
                            player.sendPluginMessage(out.toByteArray());
                        }
                        break;
                    }
                    case "remove_cosmetic": {
                        String[] args = e.getMessage().getExtraInfo().split("\n");
                        if (args.length == 2) {
                            AuroraMCProxyPlayer player = ProxyAPI.getPlayer(UUID.fromString(args[0]));
                            assert player != null;
                            Cosmetic cosmetic = AuroraMCAPI.getCosmetics().get(Integer.parseInt(args[1]));
                            player.getUnlockedCosmetics().remove(cosmetic);
                            ByteArrayDataOutput out = ByteStreams.newDataOutput();
                            out.writeUTF("CosmeticRemove");
                            out.writeUTF(player.getName());
                            out.writeInt(cosmetic.getId());
                            player.sendPluginMessage(out.toByteArray());
                        }
                        break;
                    }
                    case "set_rank": {
                        String[] args = e.getMessage().getExtraInfo().split("\n");
                        if (args.length == 2) {
                            UUID uuid = UUID.fromString(args[1]);
                            Rank rank = Rank.getByID(Integer.parseInt(args[0]));
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
                            }
                        }
                        break;
                    }
                    case "add_subrank": {
                        String[] args = e.getMessage().getExtraInfo().split("\n");
                        if (args.length == 2) {
                            UUID uuid = UUID.fromString(args[1]);
                            SubRank rank = SubRank.getByID(Integer.parseInt(args[0]));
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

                                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                out.writeUTF("AddSubRank");
                                out.writeUTF(player.getUniqueId().toString());
                                out.writeInt(rank.getId());
                                player.sendPluginMessage(out.toByteArray());
                            }
                        }
                        break;
                    }
                    case "remove_subrank": {
                        String[] args = e.getMessage().getExtraInfo().split("\n");
                        if (args.length == 2) {
                            UUID uuid = UUID.fromString(args[1]);
                            SubRank rank = SubRank.getByID(Integer.parseInt(args[0]));
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

                                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                out.writeUTF("RemoveSubRank");
                                out.writeUTF(player.getUniqueId().toString());
                                out.writeInt(rank.getId());
                                player.sendPluginMessage(out.toByteArray());
                            }
                        }
                        break;
                    }
                }
                break;
            }
            case MEDIA_RANK_JOIN_LEAVE: {
                List<AuroraMCProxyPlayer> players = ProxyAPI.getPlayers().stream().filter((AuroraMCProxyPlayer -> AuroraMCProxyPlayer.hasPermission("moderation"))).filter(AuroraMCProxyPlayer -> AuroraMCProxyPlayer.getPreferences().isSocialMediaNotificationsEnabled()).collect(Collectors.toList());
                Rank rank = Rank.valueOf(e.getMessage().getExtraInfo());
                String name = e.getMessage().getSender();
                if (e.getMessage().getCommand().equalsIgnoreCase("join")) {
                    for (AuroraMCProxyPlayer p : players) {
                        p.sendMessage(TextFormatter.pluginMessage("Media Join", String.format("%s has logged into the network (%s%s§r)", name, rank.getPrefixColor(), rank.getName())));
                    }
                } else {
                    for (AuroraMCProxyPlayer p : players) {
                        p.sendMessage(TextFormatter.pluginMessage("Media Leave", String.format("%s has logged off the network (%s%s§r)", name, rank.getPrefixColor(), rank.getName())));
                    }
                }
                break;
            }
            case STAFF_RANK_JOIN_LEAVE: {
                List<AuroraMCProxyPlayer> players = ProxyAPI.getPlayers().stream().filter((AuroraMCProxyPlayer -> AuroraMCProxyPlayer.hasPermission("staffmanagement"))).filter(AuroraMCProxyPlayer -> AuroraMCProxyPlayer.getPreferences().isStaffLoginNotificationsEnabled()).collect(Collectors.toList());
                Rank rank = Rank.valueOf(e.getMessage().getExtraInfo());
                String name = e.getMessage().getSender();
                if (e.getMessage().getCommand().equalsIgnoreCase("join")) {
                    for (AuroraMCProxyPlayer p : players) {
                        p.sendMessage(TextFormatter.pluginMessage("Staff Join", String.format("%s has logged into the network (%s%s§r)", name, rank.getPrefixColor(), rank.getName())));
                    }
                } else {
                    for (AuroraMCProxyPlayer p : players) {
                        p.sendMessage(TextFormatter.pluginMessage("Staff Leave", String.format("%s has logged off the network (%s%s§r)", name, rank.getPrefixColor(), rank.getName())));
                    }
                }
                break;
            }
            case SEND: {
                AuroraMCProxyPlayer player  = ProxyAPI.getPlayer(e.getMessage().getCommand());
                ServerInfo info = ProxyAPI.getAmcServers().get(e.getMessage().getExtraInfo());
                assert player != null;
                player.sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("You are being sent from **%s** to **%s** by **%s**.", player.getServer().getName(), info.getName(), e.getMessage().getSender())));
                player.connect(info);
                break;
            }
            case SERVER_ONLINE: {
                ProxyAPI.getCore().getLogger().info("Registering new server: " + e.getMessage().getExtraInfo());
                ServerInfo info = AuroraMCAPI.getDbManager().getServerDetailsByName(e.getMessage().getExtraInfo(), AuroraMCAPI.getInfo().getNetwork().name());
                ProxyAPI.getAmcServers().put(info.getName(), info);
                net.md_5.bungee.api.config.ServerInfo info2 = ProxyServer.getInstance().constructServerInfo(info.getName(), new InetSocketAddress(info.getIp(), info.getPort()), "", false);
                ProxyServer.getInstance().getServers().put(info.getName(), info2);
                if (info.getServerType().getString("type").equalsIgnoreCase("lobby")) {
                    new ArrayList<>(ProxyServer.getInstance().getConfig().getListeners()).get(0).getServerPriority().add(info.getName());
                    ProxyAPI.getLobbyServers().add(info);
                }
                break;
            }
            case SERVER_OFFLINE: {
                ServerInfo info = ProxyAPI.getAmcServers().remove(e.getMessage().getExtraInfo());
                if (info.getServerType().getString("type").equalsIgnoreCase("lobby")) {
                    new ArrayList<>(ProxyServer.getInstance().getConfig().getListeners()).get(0).getServerPriority().remove(info.getName());
                    ProxyAPI.getLobbyServers().removeIf(info3 -> info3.equals(info));
                }
                break;
            }
            case ALPHA_UPDATE: {
                for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                    player.disconnect(TextFormatter.pluginMessage("Server Manager", "§cThe alpha network has been closed.\n" +
                            "\n" +
                            "§rThanks for helping us test out our upcoming updates!\n" +
                            "\n" +
                            "Keep an eye on auroramc.net and our Discord server for information on when the network will be open again!"));
                }
                CommunicationUtils.sendMessage(new ProtocolMessage(Protocol.CONFIRM_SHUTDOWN, "Mission Control", "shutdown", AuroraMCAPI.getInfo().getName(), ""));
                CommunicationUtils.shutdown();
                break;
            }
            case APPROVAL_NOTIFICATION: {
                List<AuroraMCProxyPlayer> players = ProxyAPI.getPlayers().stream().filter((AuroraMCProxyPlayer -> AuroraMCProxyPlayer.hasPermission("staffmanagement"))).filter(AuroraMCProxyPlayer -> AuroraMCProxyPlayer.getPreferences().isApprovalNotificationsEnabled()).collect(Collectors.toList());
                for (AuroraMCProxyPlayer pl : players) {
                    pl.sendMessage(TextFormatter.pluginMessage("Punish", "There is a new punishment to be approved. Type /sm to view all punishments awaiting approval."));
                }
                break;
            }

            case REPORT_NOTIFICATION: {
                int id = Integer.parseInt(e.getMessage().getExtraInfo());
                PlayerReport report = AuroraMCAPI.getDbManager().getReport(id);
                BaseComponent message = TextFormatter.pluginMessage("Reports", String.format("Your report against **%s** has been §%s§r. Thank you for submitting a report!", report.getSuspectName(), ((report.getOutcome() == PlayerReport.ReportOutcome.ACCEPTED)?"aaccepted":"cdenied§r. After review, it was deemed that there was not enough evidence to punish the user at this time")));
                for (int i : report.getReporters()) {
                    AuroraMCProxyPlayer player = ProxyAPI.getPlayer(i);
                    if (player != null) {
                        player.sendMessage(message);
                    }
                }
                break;
            }
        }
    }

}
