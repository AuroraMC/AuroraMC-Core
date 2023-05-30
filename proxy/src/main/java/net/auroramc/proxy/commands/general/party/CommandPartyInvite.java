/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.proxy.commands.general.party;


import net.auroramc.api.AuroraMCAPI;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandPartyInvite extends ProxyCommand {
    public CommandPartyInvite() {
        super("invite", Collections.emptyList(), Collections.emptyList(), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
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
            player.sendMessage(TextFormatter.pluginMessage("Party", "Invalid syntax. Correct syntax: **/party invite <player>**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
