/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.proxy.commands.general.party;


import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.player.PlayerPreferences;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyAPI;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import net.auroramc.proxy.api.player.party.PartyInvite;
import net.auroramc.proxy.api.player.party.PartyPlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandParty extends ProxyCommand {


    public CommandParty() {
        super("party", Collections.singletonList("p"), Collections.singletonList(Permission.PLAYER), false, null);
        this.registerSubcommand("chat", new ArrayList<>(), new CommandPartyChat());
        this.registerSubcommand("invite", new ArrayList<>(), new CommandPartyInvite());
        this.registerSubcommand("leave", new ArrayList<>(), new CommandPartyLeave());
        this.registerSubcommand("transfer", new ArrayList<>(), new CommandPartyTransfer());
        this.registerSubcommand("accept", new ArrayList<>(), new CommandPartyAccept());
        this.registerSubcommand("deny", new ArrayList<>(), new CommandPartyDeny());
        this.registerSubcommand("cancel", new ArrayList<>(), new CommandPartyCancel());
        this.registerSubcommand("remove", Collections.singletonList("kick"), new CommandPartyRemove());
        this.registerSubcommand("warp", new ArrayList<>(), new CommandPartyWarp());
        this.registerSubcommand("disband", new ArrayList<>(), new CommandPartyDisband());
        this.registerSubcommand("list", new ArrayList<>(), new CommandPartyList());
        this.registerSubcommand("help", new ArrayList<>(), new CommandPartyHelp());
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 0) {
            player.sendMessage(TextFormatter.pluginMessage("Party", "Available commands:\n" +
                    "**/party <player>** - Alias for /party invite.\n" +
                    "**/party chat <message>** - Send a chat message into party chat.\n" +
                    "**/party leave** - Leave your current party.\n" +
                    "**/party invite <player>** - Invite a player to your party.\n" +
                    "**/party transfer <player>** - Transfer your party to someone else.\n" +
                    "**/party accept <player>** - Accept a players party invite.\n" +
                    "**/party deny <player>** - Deny a players party invite.\n" +
                    "**/party cancel <player>** - Cancel an outgoing party invite.\n" +
                    "**/party remove <player>** - Remove a player from your party.\n" +
                    "**/party warp** - Warp all players to your current server.\n" +
                    "**/party list** - List all players in your current party.\n" +
                    "**/party disband** - Disband your party.\n" +
                    "**/party help** - Displays this help menu."));
            return;
        }
        switch (args.get(0).toLowerCase()) {
            case "help":
            case "chat":
            case "leave":
            case "invite":
            case "transfer":
            case "accept":
            case "deny":
            case "cancel":
            case "remove":
            case "kick":
            case "warp":
            case "list":
            case "disband":
                aliasUsed = args.remove(0).toLowerCase();
                subcommands.get(aliasUsed).execute(player, aliasUsed, args);
                break;
            default:
                if (args.size() == 1) {
                    if (player.getParty() != null) {
                        if (!player.getParty().getLeader().getUuid().equals(player.getUniqueId())) {
                            player.sendMessage(TextFormatter.pluginMessage("Party", "You must be the leader of the party in order to invite people to it!"));
                            return;
                        }
                        int limit = 9; //-1 to account for the leader of the party.
                        if (player.hasPermission("elite")) {
                            limit += 5;
                        }
                        if (player.hasPermission("master")) {
                            limit += 5;
                        }
                        if (player.hasPermission("plus")) {
                            limit += 15;
                        }

                        if (player.getParty().getPlayers().size() + player.getParty().getPartyInvites().size() >= limit) {
                            player.sendMessage(TextFormatter.pluginMessage("Party", "You have reached your party size limit. Remove existing members, cancel outgoing party invites or purchase a rank upgrade at store.auroramc.net in order to make space in your party."));
                            return;
                        }
                    }
                    player.sendMessage(TextFormatter.pluginMessage("Party", String.format("Searching the network for **%s**...", args.get(0))));

                    //Invite.
                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args.get(0));
                    if (target != null) {
                        if (target.equals(player)) {
                            player.sendMessage(TextFormatter.pluginMessage("Party", "You cannot invite yourself to a party."));
                            return;
                        }
                        AuroraMCProxyPlayer aTarget = ProxyAPI.getPlayer(target);
                        if (aTarget != null) {
                            if (player.getParty() == null) {
                                if (!aTarget.getPreferences().isPartyRequestsEnabled()) {
                                    player.sendMessage(TextFormatter.pluginMessage("Party", "That player has party requests disabled!"));
                                    return;
                                }
                                if (aTarget.getPartyPlayer() != null) {
                                    player.createdParty(aTarget.getPartyPlayer());
                                } else {
                                    player.createdParty(new PartyPlayer(aTarget.getId(), aTarget.getName(), aTarget.getUniqueId(), aTarget, UUID.fromString(AuroraMCAPI.getInfo().getName()), aTarget.getRank()));
                                }
                            } else {
                                for (PartyPlayer pl : player.getParty().getPlayers()) {
                                    if (pl.getUuid().equals(aTarget.getUniqueId())) {
                                        player.sendMessage(TextFormatter.pluginMessage("Party", "That player is already in your party."));
                                        return;
                                    }
                                }

                                for (PartyInvite pi : player.getParty().getPartyInvites()) {
                                    if (pi.getPlayer().getUuid().equals(aTarget.getUniqueId())) {
                                        player.sendMessage(TextFormatter.pluginMessage("Party", "That player is already invited to your party."));
                                        return;
                                    }
                                }

                                if (!aTarget.getPreferences().isPartyRequestsEnabled()) {
                                    player.sendMessage(TextFormatter.pluginMessage("Party", "That player has party requests disabled!"));
                                    return;
                                }
                                if (aTarget.getPartyPlayer() != null) {
                                    player.getParty().newInvite(aTarget.getPartyPlayer());
                                } else {
                                    player.getParty().newInvite(new PartyPlayer(aTarget.getId(), aTarget.getName(), aTarget.getUniqueId(), aTarget, UUID.fromString(AuroraMCAPI.getInfo().getName()), aTarget.getRank()));
                                }
                            }
                        } else {
                            player.sendMessage(TextFormatter.pluginMessage("Party", String.format("No matches found for [**%s**]", args.get(0))));
                        }
                    } else {
                        //No match was found on this proxy, attempt on the entire network.
                        ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                            UUID uuid = AuroraMCAPI.getDbManager().getUUID(args.get(0));
                            if (uuid != null) {
                                if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                                    if (player.getParty() != null) {
                                        for (PartyPlayer pl : player.getParty().getPlayers()) {
                                            if (pl.getUuid().equals(uuid)) {
                                                player.sendMessage(TextFormatter.pluginMessage("Party", "That player is already in your party."));
                                                return;
                                            }
                                        }

                                        for (PartyInvite pi : player.getParty().getPartyInvites()) {
                                            if (pi.getPlayer().getUuid().equals(uuid)) {
                                                player.sendMessage(TextFormatter.pluginMessage("Party", "That player is already invited to your party."));
                                                return;
                                            }
                                        }
                                    }
                                    PlayerPreferences playerPreferences = AuroraMCAPI.getDbManager().getPlayerPreferences(uuid);
                                    if (playerPreferences.isPartyRequestsEnabled()) {

                                        int id = AuroraMCAPI.getDbManager().getAuroraMCID(uuid);
                                        String name = AuroraMCAPI.getDbManager().getNameFromID(id);
                                        UUID proxyUUID = AuroraMCAPI.getDbManager().getProxy(uuid);
                                        Rank rank = AuroraMCAPI.getDbManager().getRank(uuid);
                                        PartyPlayer partyPlayer = new PartyPlayer(id, name, uuid, null, proxyUUID, rank);
                                        if (player.getParty() != null) {
                                            player.getParty().newInvite(partyPlayer);
                                        } else {
                                            player.createdParty(partyPlayer);
                                        }
                                    } else {
                                        player.sendMessage(TextFormatter.pluginMessage("Party", "That player has party requests disabled!"));
                                    }
                                } else {
                                    player.sendMessage(TextFormatter.pluginMessage("Party", String.format("No matches found for [**%s**]", args.get(0))));
                                }
                            } else {
                                player.sendMessage(TextFormatter.pluginMessage("Party", String.format("No matches found for [**%s**]", args.get(0))));
                            }

                        });

                    }
                } else {
                    player.sendMessage(TextFormatter.pluginMessage("Party", "Invalid subcommand. Available commands:\n" +
                            "**/party <player>** - Alias for /party invite.\n" +
                            "**/party chat <message>** - Send a chat message into party chat.\n" +
                            "**/party leave** - Leave your current party.\n" +
                            "**/party invite <player>** - Invite a player to your party.\n" +
                            "**/party transfer <player>** - Transfer your party to someone else.\n" +
                            "**/party accept <player>** - Accept a players party invite.\n" +
                            "**/party deny <player>** - Deny a players party invite.\n" +
                            "**/party cancel <player>** - Cancel an outgoing party invite.\n" +
                            "**/party remove <player>** - Remove a player from your party.\n" +
                            "**/party warp** - Warp all players to your current server.\n" +
                            "**/party list** - List all players in your current party.\n" +
                            "**/party disband** - Disband your party.\n" +
                            "**/party help** - Displays this help menu."));
                }
                break;
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        List<String> types = new ArrayList<>(Arrays.asList("chat", "leave", "invite", "transfer", "accept", "deny", "cancel", "warp", "list"));
        List<String> completions = new ArrayList<>();
        if (numberArguments == 1) {
            for (String type : types) {
                if (type.toLowerCase().startsWith(lastToken.toLowerCase())) {
                    completions.add(type);
                }
            }
        } else if (numberArguments >= 2) {
            switch (args.get(0).toLowerCase()) {
                case "chat":
                case "leave":
                case "invite":
                case "transfer":
                case "accept":
                case "deny":
                case "cancel":
                case "warp":
                case "list":
                    aliasUsed = args.remove(0);
                    return subcommands.get(aliasUsed).onTabComplete(player, aliasUsed, args, ((args.size() >= 1)?args.get(0):""), numberArguments - 1);
                default:
                    return new ArrayList<>();
            }
        }

        return completions;
    }
}
