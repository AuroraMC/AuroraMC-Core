/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.proxy.api.player.party;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.backend.ChatLogs;
import net.auroramc.api.backend.info.ServerInfo;
import net.auroramc.api.player.ChatChannel;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyAPI;
import net.auroramc.proxy.api.backend.communication.CommunicationUtils;
import net.auroramc.proxy.api.backend.communication.Protocol;
import net.auroramc.proxy.api.backend.communication.ProtocolMessage;
import net.auroramc.proxy.api.backend.ProxyDatabaseManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Party {

    private final UUID uuid;
    private PartyPlayer leader;
    private final List<PartyPlayer> players;
    private final List<PartyInvite> partyInvites;

    //TODO: rewrite parties to work over connection nodes.

    public Party(PartyPlayer leader, PartyPlayer invitee) {
        this.uuid = UUID.randomUUID();
        this.leader = leader;
        if (leader.getPlayer() != null) {
            leader.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", "You have created a party!"));
        }
        players = new ArrayList<>();
        partyInvites = new ArrayList<>();
        ProxyAPI.getParties().put(this.uuid, this);
        partyInvites.add(new PartyInvite(this, invitee));
        ProxyDatabaseManager.newParty(this);
        if (invitee.getPlayer() == null) {
            ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                ProtocolMessage message = new ProtocolMessage(Protocol.PARTY, invitee.getProxyUUID().toString(), "new_invite", AuroraMCAPI.getInfo().getName(), this.uuid + "\n" + invitee.getUuid());
                CommunicationUtils.sendMessage(message);
            });
        }
    }

    public Party(UUID uuid, PartyPlayer leader, List<PartyPlayer> players, List<PartyInvite> partyInvites) {
        this.uuid = uuid;
        this.leader = leader;
        this.players = players;
        this.partyInvites = partyInvites;
        for (PartyInvite inv : partyInvites) {
            inv.setParty(this);
        }
        ProxyAPI.getParties().put(this.uuid, this);
    }



    public PartyPlayer getLeader() {
        return leader;
    }

    public List<PartyPlayer> getPlayers() {
        return new ArrayList<>(players);
    }

    public List<PartyInvite> getPartyInvites() {
        return new ArrayList<>(partyInvites);
    }

    public void removePartyInvite(PartyInvite invite) {
        partyInvites.remove(invite);
        invite.responded(false);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void newInvite(PartyPlayer player) {
        partyInvites.add(new PartyInvite(this, player));
        List<UUID> proxiesToSend = new ArrayList<>();
        for (PartyPlayer member : players) {
            if (member.getPlayer() == null) {
                if (!proxiesToSend.contains(member.getProxyUUID())) {
                    proxiesToSend.add(member.getProxyUUID());
                }
            } else {
                member.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", String.format("**%s** has been invited to the party!", player.getName())));
            }
        }
        for (PartyInvite invite : partyInvites) {
            if (!proxiesToSend.contains(invite.getPlayer().getProxyUUID()) && !invite.getPlayer().getProxyUUID().equals(UUID.fromString(AuroraMCAPI.getInfo().getName()))) {
                proxiesToSend.add(invite.getPlayer().getProxyUUID());
            }
        }
        if (player.getPlayer() == null && !proxiesToSend.contains(player.getProxyUUID())) {
            proxiesToSend.add(player.getProxyUUID());
        }
        for (UUID uuid : proxiesToSend) {
            if (uuid.equals(UUID.fromString(AuroraMCAPI.getInfo().getName()))) {
                continue;
            }
            ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                ProtocolMessage message = new ProtocolMessage(Protocol.PARTY, uuid.toString(), "new_invite", AuroraMCAPI.getInfo().getName(), this.uuid.toString() + "\n" + player.getUuid());
                CommunicationUtils.sendMessage(message);
            });
        }
        ProxyDatabaseManager.updatePartyInvites(this);
    }

    public void newInvite(PartyInvite invite) {
        partyInvites.add(invite);
        for (PartyPlayer member : players) {
            if (member.getPlayer() != null) {
                member.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", String.format("**%s** has been invited to the party!", invite.getPlayer().getName())));
            }
        }
    }

    public void expired(PartyInvite partyInvite) {
        this.partyInvites.remove(partyInvite);
        if (leader.getPlayer() != null) {
            ProxyDatabaseManager.updatePartyInvites(this);
        }
        //Checking to see if any of the players in the party are online on this proxy. If not, remove it from the cache.
        for (PartyPlayer player : players) {
            if (player.getPlayer() != null) {
                if (leader.getPlayer() != null) {
                    leader.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", String.format("Your party invite to **%s** expired.", partyInvite.getPlayer().getName())));
                }
                return;
            }
        }
        if (leader.getPlayer() == null) {
            ProxyAPI.getParties().remove(this.uuid);
        } else {
            leader.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", String.format("Your party invite to **%s** expired.", partyInvite.getPlayer().getName())));
        }
        if (players.size() == 0 && partyInvites.size() == 0) {
            if (leader.getPlayer() != null) {
                leader.getPlayer().leftParty(true);
                leader.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", "You are the only one left in the party, so it was disband!"));
                ProxyDatabaseManager.disbandParty(this);
            }
            ProxyAPI.getParties().remove(this.uuid);
        }
    }

    public void requestAccepted(PartyInvite partyInvite) {
        this.players.add(partyInvite.getPlayer());
        partyInvite.responded(true);
        partyInvites.remove(partyInvite);
        PartyPlayer player = partyInvite.getPlayer();
        List<UUID> proxiesToSend = new ArrayList<>();
        for (PartyPlayer member : players) {
            if (member.getPlayer() != null) {
                member.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", String.format("**%s** has joined the party!", player.getName())));
            } else {
                if (!proxiesToSend.contains(member.getProxyUUID())) {
                    proxiesToSend.add(member.getProxyUUID());
                }
            }
        }
        for (PartyInvite invite : partyInvites) {
            if (invite.getPlayer() == null) {
                if (!proxiesToSend.contains(invite.getPlayer().getProxyUUID()) && !invite.getPlayer().getProxyUUID().equals(UUID.fromString(AuroraMCAPI.getInfo().getName()))) {
                    proxiesToSend.add(invite.getPlayer().getProxyUUID());
                }
            }
        }
        if (leader.getPlayer() != null) {
            leader.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", String.format("**%s** has joined the party!", player.getName())));
            int limit = 9; //-1 to account for the leader of the party.
            if (leader.getPlayer().hasPermission("elite")) {
                limit += 5;
            }
            if (leader.getPlayer().hasPermission("master")) {
                limit += 5;
            }
            if (leader.getPlayer().hasPermission("plus")) {
                limit += 15;
            }
            if (limit == players.size()) {
                if (!leader.getPlayer().getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(39))) {
                    leader.getPlayer().getStats().achievementGained(AuroraMCAPI.getAchievement(39), 1, true);
                }
            }
        } else if (!proxiesToSend.contains(leader.getProxyUUID())) {
            proxiesToSend.add(leader.getProxyUUID());
        }
        for (UUID uuid : proxiesToSend) {
            if (uuid.equals(UUID.fromString(AuroraMCAPI.getInfo().getName()))) {
                continue;
            }
            ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                ProtocolMessage message = new ProtocolMessage(Protocol.PARTY, uuid.toString(), "invite_accepted", AuroraMCAPI.getInfo().getName(), this.uuid.toString() + "\n" + player.getUuid());
                CommunicationUtils.sendMessage(message);
            });
        }
        ProxyDatabaseManager.updateMembers(this);
    }

    public void requestAccepted(PartyPlayer player) {
        this.players.add(player);
        List<PartyInvite> invites = new ArrayList<>(partyInvites);
        for (PartyInvite invite : invites) {
            if (invite.getPlayer().getUuid().equals(player.getUuid())) {
                invite.responded(true);
                partyInvites.remove(invite);
                break;
            }
        }
        for (PartyPlayer member : players) {
            if (member.getPlayer() != null) {
                member.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", String.format("**%s** has joined the party!", player.getName())));
            }
        }
        if (leader.getPlayer() != null) {
            leader.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", String.format("**%s** has joined the party!", player.getName())));
            int limit = 9; //-1 to account for the leader of the party.
            if (leader.getPlayer().hasPermission("elite")) {
                limit += 5;
            }
            if (leader.getPlayer().hasPermission("master")) {
                limit += 5;
            }
            if (leader.getPlayer().hasPermission("plus")) {
                limit += 15;
            }
            if (limit == players.size()) {
                if (!leader.getPlayer().getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(39))) {
                    leader.getPlayer().getStats().achievementGained(AuroraMCAPI.getAchievement(39), 1, true);
                }
            }
        }
    }

    public void requestDenied(PartyInvite partyInvite) {
        List<UUID> proxiesToSend = new ArrayList<>();

        for (PartyPlayer player : players) {
            if (!proxiesToSend.contains(partyInvite.getPlayer().getProxyUUID()) && !player.getProxyUUID().equals(UUID.fromString(AuroraMCAPI.getInfo().getName()))) {
                proxiesToSend.add(partyInvite.getPlayer().getProxyUUID());
            }
        }

        for (PartyInvite invite : partyInvites) {
            if (invite.getPlayer() == null) {
                if (!proxiesToSend.contains(invite.getPlayer().getProxyUUID()) && !invite.getPlayer().getProxyUUID().equals(UUID.fromString(AuroraMCAPI.getInfo().getName()))) {
                    proxiesToSend.add(invite.getPlayer().getProxyUUID());
                }
            }
        }

        partyInvite.responded(false);
        partyInvites.remove(partyInvite);

        for (UUID uuid : proxiesToSend) {
            if (uuid.equals(UUID.fromString(AuroraMCAPI.getInfo().getName()))) {
                continue;
            }
            ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                ProtocolMessage message = new ProtocolMessage(Protocol.PARTY, uuid.toString(), "invite_cancelled", UUID.fromString(AuroraMCAPI.getInfo().getName()).toString(), this.uuid.toString() + "\n" + partyInvite.getPlayer().getUuid());
                CommunicationUtils.sendMessage(message);
            });
        }

        for (PartyPlayer player : players) {
            if (player.getPlayer() != null) {
                return;
            }
        }

        if (leader.getPlayer() == null) {
            ProtocolMessage message = new ProtocolMessage(Protocol.PARTY, leader.getProxyUUID().toString(), "invite_cancelled", UUID.fromString(AuroraMCAPI.getInfo().getName()).toString(), this.uuid.toString() + "\n" + partyInvite.getPlayer().getUuid());
            CommunicationUtils.sendMessage(message);
            ProxyAPI.getParties().remove(this.uuid);
        } else {
            leader.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", String.format("Your party invite to **%s** was declined.", partyInvite.getPlayer().getName())));
        }

        if (players.size() == 0 && partyInvites.size() == 0) {
            if (leader.getPlayer() != null) {
                leader.getPlayer().leftParty(true);
                leader.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", "You are the only one left in the party, so it was disbanded!"));
            } else {
                ProtocolMessage message = new ProtocolMessage(Protocol.PARTY, leader.getProxyUUID().toString(), "disband_left", UUID.fromString(AuroraMCAPI.getInfo().getName()).toString(), this.uuid.toString());
                CommunicationUtils.sendMessage(message);
            }
            ProxyAPI.getParties().remove(this.uuid);
            ProxyDatabaseManager.disbandParty(this);
        }
    }

    public void requestDenied(PartyPlayer partyPlayer) {
        List<PartyInvite> invites = new ArrayList<>(partyInvites);
        for (PartyInvite invite : invites) {
            if (invite.getPlayer().getUuid().equals(partyPlayer.getUuid())) {
                invite.responded(false);
                partyInvites.remove(invite);
                if (partyPlayer.getPlayer() != null) {
                    partyPlayer.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", String.format("Your party invite from **%s** was cancelled.", leader.getName())));
                }
                if (leader.getPlayer() != null) {
                    leader.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", String.format("Your party invite to **%s** was declined.", partyPlayer.getName())));
                }
                break;
            }
        }

        for (PartyPlayer player : players) {
            if (player.getPlayer() != null) {
                return;
            }
        }
        if (leader.getPlayer() == null) {
            ProxyAPI.getParties().remove(this.uuid);
        }
    }

    public void disband() {
        if (leader.getPlayer() != null) {
            leader.getPlayer().leftParty(true);
            leader.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", "The leader has disbanded the party!"));
            ProxyDatabaseManager.disbandParty(this);
        }

        List<UUID> proxiesToSend = new ArrayList<>();

        for (PartyPlayer player : players) {
            if (player.getPlayer() != null) {
                player.getPlayer().leftParty(true);
                player.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", "The leader has disbanded the party!"));
            } else {
                if (leader.getPlayer() != null) {
                    if (!proxiesToSend.contains(player.getProxyUUID())) {
                        proxiesToSend.add(player.getProxyUUID());
                    }
                }
            }
        }

        if (leader.getPlayer() != null) {
            for (PartyInvite invite : partyInvites) {
                invite.responded(false);
                if (invite.getPlayer() != null) {
                    invite.getPlayer().getPlayer().sendMessage(TextFormatter.pluginMessage("Party", String.format("Your party invite from **%s** was cancelled as the party was disbanded.", leader.getName())));
                } else {
                    if (!proxiesToSend.contains(invite.getPlayer().getProxyUUID())) {
                        proxiesToSend.add(invite.getPlayer().getProxyUUID());
                    }
                }
            }

            for (UUID proxy : proxiesToSend) {
                if (uuid.equals(UUID.fromString(AuroraMCAPI.getInfo().getName()))) {
                    continue;
                }
                ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                    ProtocolMessage message = new ProtocolMessage(Protocol.PARTY, proxy.toString(), "disband", UUID.fromString(AuroraMCAPI.getInfo().getName()).toString(), this.uuid.toString());
                    CommunicationUtils.sendMessage(message);
                });
            }
        }

        ProxyAPI.getParties().remove(this.uuid);
    }

    public void leave(PartyPlayer player, boolean sendToServer) {
        if (leader.getPlayer() != null) {
            if (players.size() == 0 && leader.equals(player)) {
                leader.getPlayer().leftParty(sendToServer);
                List<UUID> uuidsToSend = new ArrayList<>();
                for (PartyInvite invite : partyInvites) {
                    invite.responded(false);
                    if (invite.getPlayer() != null) {
                        invite.getPlayer().getPlayer().sendMessage(TextFormatter.pluginMessage("Party", String.format("Your party invite from **%s** was cancelled as the party was disbanded.", leader.getName())));
                    } else {
                        if (!uuidsToSend.contains(invite.getPlayer().getProxyUUID())) {
                            uuidsToSend.add(invite.getPlayer().getProxyUUID());
                        }
                    }
                }
                for (UUID uuid : uuidsToSend) {
                    if (uuid.equals(UUID.fromString(AuroraMCAPI.getInfo().getName()))) {
                        continue;
                    }
                    ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                        ProtocolMessage message = new ProtocolMessage(Protocol.PARTY, uuid.toString(), "disband_left", UUID.fromString(AuroraMCAPI.getInfo().getName()).toString(), this.uuid.toString());
                        CommunicationUtils.sendMessage(message);
                    });
                }
                ProxyDatabaseManager.disbandParty(this);
                ProxyAPI.getParties().remove(this.uuid);
                return;
            }
            if (leader.equals(player)) {
                if (players.size() == 1 && partyInvites.size() == 0) {
                    //Disband
                    if (players.get(0).getPlayer() != null) {
                        players.get(0).getPlayer().leftParty(true);
                        players.get(0).getPlayer().sendMessage(TextFormatter.pluginMessage("Party", String.format("**%s** has left the party!", player.getName())));
                        players.get(0).getPlayer().sendMessage(TextFormatter.pluginMessage("Party", "You are the only one left in the party, so it was disbanded!"));
                    } else {
                        ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                            ProtocolMessage message = new ProtocolMessage(Protocol.PARTY, players.get(0).getProxyUUID().toString(), "disband_left", UUID.fromString(AuroraMCAPI.getInfo().getName()).toString(), this.uuid.toString());
                            CommunicationUtils.sendMessage(message);
                        });
                    }
                    leader.getPlayer().leftParty(sendToServer);
                    if (leader.getPlayer().isOnline()) {
                        leader.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", String.format("**%s** has left the party!", player.getName())));
                    }
                    ProxyDatabaseManager.disbandParty(this);
                    ProxyAPI.getParties().remove(this.uuid);
                } else {
                    PartyPlayer oldLeader = leader;
                    leader.getPlayer().leftParty(sendToServer);
                    leader = players.remove(0);
                    List<UUID> uuidsToSend = new ArrayList<>();
                    for (PartyPlayer pl : players) {
                        if (pl.getPlayer() != null) {
                            pl.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", String.format("Party leadership was passed to **%s**.", leader.getName())));
                            pl.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", String.format("**%s** has left the party!", player.getName())));
                        } else {
                            if (!uuidsToSend.contains(pl.getProxyUUID())) {
                                uuidsToSend.add(pl.getProxyUUID());
                            }
                        }
                    }
                    if (leader.getPlayer() != null) {
                        leader.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", String.format("**%s** has left the party!", player.getName())));
                        leader.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", String.format("Party leadership was passed to **%s**.", leader.getName())));
                    } else {
                        if (!uuidsToSend.contains(leader.getProxyUUID())) {
                            uuidsToSend.add(leader.getProxyUUID());
                        }
                    }
                    ProxyDatabaseManager.updateLeader(this);
                    ProxyDatabaseManager.updateMembers(this);
                    if (oldLeader.getPlayer() != null) {
                        for (UUID uuid : uuidsToSend) {
                            if (uuid.equals(UUID.fromString(AuroraMCAPI.getInfo().getName()))) {
                                continue;
                            }
                            ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                                ProtocolMessage message = new ProtocolMessage(Protocol.PARTY, uuid.toString(), "transfer", UUID.fromString(AuroraMCAPI.getInfo().getName()).toString(), this.uuid.toString());
                                CommunicationUtils.sendMessage(message);
                                message = new ProtocolMessage(Protocol.PARTY, uuid.toString(), "left", UUID.fromString(AuroraMCAPI.getInfo().getName()).toString(), this.uuid.toString() + "\n" + player.getUuid().toString());
                                CommunicationUtils.sendMessage(message);
                            });
                        }
                    }
                    if (player.getPlayer() != null) {
                        player.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", String.format("**%s** has left the party!", player.getName())));
                        player.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", String.format("Party leadership was passed to **%s**.", leader.getName())));
                    }

                    for (PartyPlayer player1 : players) {
                        if (player1.getPlayer() != null) {
                            return;
                        }
                    }

                    if (leader.getPlayer() == null) {
                        ProxyAPI.getParties().remove(this.uuid);
                    }
                }
                return;
            }
        }
        players.remove(player);
        if (player.getPlayer() != null) {
            player.getPlayer().leftParty(sendToServer);
            if (player.getPlayer().isOnline()) {
                player.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", String.format("**%s** has left the party!", player.getName())));
            }
        }

        if (players.size() == 0 && partyInvites.size() == 0) {
            //disband
            if (leader.getPlayer() != null) {
                leader.getPlayer().leftParty(true);
                leader.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", String.format("**%s** has left the party!", player.getName())));
                leader.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", "You are the only one left in the party, so it was disbanded!"));
                ProxyDatabaseManager.disbandParty(this);
            } else {
                ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                    ProtocolMessage message = new ProtocolMessage(Protocol.PARTY, leader.getProxyUUID().toString(), "disband_left", UUID.fromString(AuroraMCAPI.getInfo().getName()).toString(), this.uuid.toString());
                    CommunicationUtils.sendMessage(message);
                });
            }
            ProxyAPI.getParties().remove(this.uuid);
            return;
        } else {
            List<UUID> uuidsToSend = new ArrayList<>();
            for (PartyPlayer member : players) {
                if (member.getPlayer() != null) {
                    member.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", String.format("**%s** has left the party!", player.getName())));
                } else {
                    if (player.getPlayer() != null && !uuidsToSend.contains(member.getProxyUUID())) {
                        uuidsToSend.add(member.getProxyUUID());
                    }
                }
            }
            if (player.getPlayer() == null) {
                if (!uuidsToSend.contains(player.getProxyUUID())) {
                    uuidsToSend.add(player.getProxyUUID());
                }
            }
            if (leader.getPlayer() != null) {
                leader.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", String.format("**%s** has left the party!", player.getName())));
            } else if (!uuidsToSend.contains(leader.getProxyUUID())) {
                uuidsToSend.add(leader.getProxyUUID());
            }

            if (player.getPlayer() != null) {
                for (UUID uuid : uuidsToSend) {
                    if (uuid.equals(UUID.fromString(AuroraMCAPI.getInfo().getName()))) {
                        continue;
                    }
                    ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                        ProtocolMessage message = new ProtocolMessage(Protocol.PARTY, uuid.toString(), "left", UUID.fromString(AuroraMCAPI.getInfo().getName()).toString(), this.uuid.toString() + "\n" + player.getUuid().toString());
                        CommunicationUtils.sendMessage(message);
                    });
                }
            }
        }

        if (leader.getPlayer() != null) {
            ProxyDatabaseManager.updateMembers(this);
        }

        for (PartyPlayer player1 : players) {
            if (player1.getPlayer() != null) {
                return;
            }
        }

        if (leader.getPlayer() == null) {
            ProxyAPI.getParties().remove(this.uuid);
        }
    }

    public void remove(PartyPlayer player, boolean sendToServer) {
        players.remove(player);
        if (player.getPlayer() != null) {
            player.getPlayer().leftParty(sendToServer);
            if (player.getPlayer().isOnline()) {
                player.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", String.format("**%s** has been kicked from the party!", player.getName())));
                player.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", "You have been removed from the party by the party leader."));
            }
        }

        if (players.size() == 0 && partyInvites.size() == 0) {
            //disband
            if (leader.getPlayer() != null) {
                leader.getPlayer().leftParty(true);
                leader.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", String.format("**%s** has been kicked from the party!", player.getName())));
                leader.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", "You are the only one left in the party, so it was disbanded!"));
                if (player.getPlayer() == null) {
                    ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                        ProtocolMessage message = new ProtocolMessage(Protocol.PARTY, player.getProxyUUID().toString(), "remove", UUID.fromString(AuroraMCAPI.getInfo().getName()).toString(), this.uuid.toString() + "\n" + player.getUuid().toString());
                        CommunicationUtils.sendMessage(message);
                    });
                }
                ProxyDatabaseManager.disbandParty(this);
            }
            ProxyAPI.getParties().remove(this.uuid);
            return;
        } else {
            List<UUID> uuidsToSend = new ArrayList<>();
            for (PartyPlayer member : players) {
                if (member.getPlayer() != null) {
                    member.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", String.format("**%s** has kicked from the party!", player.getName())));
                } else {
                    if (!uuidsToSend.contains(member.getProxyUUID())) {
                        uuidsToSend.add(member.getProxyUUID());
                    }
                }
            }


            if (player.getPlayer() == null) {
                if (!uuidsToSend.contains(player.getProxyUUID())) {
                    uuidsToSend.add(player.getProxyUUID());
                }
            }
            if (leader.getPlayer() != null) {
                leader.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", String.format("**%s** has been kicked from the party!", player.getName())));
                for (UUID uuid : uuidsToSend) {
                    if (uuid.equals(UUID.fromString(AuroraMCAPI.getInfo().getName()))) {
                        continue;
                    }
                    ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                        ProtocolMessage message = new ProtocolMessage(Protocol.PARTY, uuid.toString(), "remove", UUID.fromString(AuroraMCAPI.getInfo().getName()).toString(), this.uuid.toString() + "\n" + player.getUuid().toString());
                        CommunicationUtils.sendMessage(message);
                    });
                }
            }
        }

        if (leader.getPlayer() != null) {
            ProxyDatabaseManager.updateMembers(this);
        }

        for (PartyPlayer player1 : players) {
            if (player1.getPlayer() != null) {
                return;
            }
        }

        if (leader.getPlayer() == null) {
            ProxyAPI.getParties().remove(this.uuid);
        }
    }

    public void disbandLeft() {
        for (PartyPlayer player : players) {
            if (player.getPlayer() != null) {
                player.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", String.format("**%s** has left the party!", leader.getName())));
                player.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", "You are the only one left in the party, so it was disbanded!"));
                player.getPlayer().leftParty(true);
                ProxyAPI.getParties().remove(this.uuid);
                return;
            }
        }
        if (players.size() != 0) {
            leader.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", String.format("**%s** has left the party!", players.get(0).getName())));
        }
        leader.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", "You are the only one left in the party, so it was disbanded!"));
        leader.getPlayer().leftParty(true);
        ProxyAPI.getParties().remove(this.uuid);
    }

    public void partyChat(PartyPlayer sender, BaseComponent message, String filteredMessage) {
        BaseComponent component = TextFormatter.formatPartyChat(sender.getRank(), sender.getName(), message);

        ChatLogs.chatMessage(sender.getId(), sender.getName(), sender.getRank(), filteredMessage, false, ChatChannel.PARTY, -1, null, uuid);

        List<UUID> uuidsToSend = new ArrayList<>();
        for (PartyPlayer player : players) {
            if (player.getPlayer() != null) {
                player.getPlayer().sendMessage(component);
                if (player.getPlayer().getPreferences().isPingOnPartyChatEnabled()) {
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("PlaySound");
                    out.writeUTF(player.getName());
                    out.writeUTF("NOTE_PLING");
                    out.writeInt(100);
                    out.writeInt(2);
                    player.getPlayer().sendPluginMessage(out.toByteArray());
                }
            } else {
                if (!uuidsToSend.contains(player.getProxyUUID())) {
                    uuidsToSend.add(player.getProxyUUID());
                }
            }
        }
        if (leader.getPlayer() != null) {
            leader.getPlayer().sendMessage(component);
            if (leader.getPlayer().getPreferences().isPingOnPartyChatEnabled()) {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("PlaySound");
                out.writeUTF(leader.getName());
                out.writeUTF("NOTE_PLING");
                out.writeInt(100);
                out.writeInt(2);
                leader.getPlayer().sendPluginMessage(out.toByteArray());
            }
        } else if (!uuidsToSend.contains(leader.getProxyUUID())) {
            uuidsToSend.add(leader.getProxyUUID());
        }

        if (sender.getPlayer() != null) {
            for (UUID uuid : uuidsToSend) {
                ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                    ProtocolMessage protocolMessage = new ProtocolMessage(Protocol.PARTY, uuid.toString(), "message", UUID.fromString(AuroraMCAPI.getInfo().getName()).toString(), this.uuid.toString() + "\n" + sender.getUuid().toString() + "\n" + ComponentSerializer.toString(message) + "\n" + filteredMessage);
                    CommunicationUtils.sendMessage(protocolMessage);
                });
            }
        }
    }

    public void transfer(PartyPlayer player) {
        if (!this.players.contains(player)) {
            return;
        }
        PartyPlayer oldLeader = this.leader;

        this.players.add(leader);
        this.players.remove(player);
        this.leader = player;

        if (oldLeader.getPlayer() != null) {
            ProxyDatabaseManager.updateLeader(this);
            ProxyDatabaseManager.updateMembers(this);
        }

        List<UUID> uuidsToSend = new ArrayList<>();
        for (PartyPlayer member : players) {
            if (member.getPlayer() != null) {
                member.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", String.format("The party has been transferred to **%s**!", player.getName())));
            } else if (oldLeader.getPlayer() != null && !uuidsToSend.contains(member.getProxyUUID())) {
                uuidsToSend.add(member.getProxyUUID());
            }
        }
        if (leader.getPlayer() != null) {
            leader.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", "The party was transferred to you!"));
        } else if (oldLeader.getPlayer() != null && !uuidsToSend.contains(leader.getProxyUUID())) {
            uuidsToSend.add(leader.getProxyUUID());
        }

        for (UUID uuid : uuidsToSend) {
            ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                ProtocolMessage protocolMessage = new ProtocolMessage(Protocol.PARTY, uuid.toString(), "transfer", UUID.fromString(AuroraMCAPI.getInfo().getName()).toString(), this.uuid.toString());
                CommunicationUtils.sendMessage(protocolMessage);
            });
        }
    }

    public void warp(ServerInfo target) {
        List<UUID> uuidsToSend = new ArrayList<>();
        for (PartyPlayer player : players) {
            if (player.getPlayer() != null) {
                if (!player.getPlayer().getServer().getName().equalsIgnoreCase(target.getName())) {
                    switch (((String) target.getServerType().get("type")).toLowerCase()) {
                        case "build":
                            if (!player.getPlayer().hasPermission("build") && !player.getPlayer().hasPermission("admin")) {
                                player.getPlayer().sendMessage(TextFormatter.pluginMessage("Server Manager", "You do not have permission to go to that server!"));
                                continue;
                            }
                            player.getPlayer().connect(target);
                            player.getPlayer().sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("You are being sent from **%s** to **%s**.", player.getPlayer().getServer().getName(), target.getName())));
                            break;
                        case "staff":
                            if (!player.getPlayer().hasPermission("moderation")) {
                                player.getPlayer().sendMessage(TextFormatter.pluginMessage("Server Manager", "You do not have permission to go to that server!"));
                                continue;
                            }
                            player.getPlayer().connect(target);
                            player.getPlayer().sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("You are being sent from **%s** to **%s**.", player.getPlayer().getServer().getName(), target.getName())));
                            break;
                        case "smp": {
                            player.getPlayer().sendMessage(TextFormatter.pluginMessage("Server Manager", "You cannot manually go to an SMP server. You must use the Lobby to connect to SMP."));
                            return;
                        }
                        default:
                            player.getPlayer().connect(target);
                            player.getPlayer().sendMessage(TextFormatter.pluginMessage("Server Manager", String.format("You are being sent from **%s** to **%s**.", player.getPlayer().getServer().getName(), target.getName())));
                    }

                }
                player.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", "The party leader warped everyone to their server!"));
            } else {
                if (leader.getPlayer() != null && !uuidsToSend.contains(player.getProxyUUID())) {
                    uuidsToSend.add(player.getProxyUUID());
                }
            }
        }

        for (UUID uuid : uuidsToSend) {
            ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                ProtocolMessage protocolMessage = new ProtocolMessage(Protocol.PARTY, uuid.toString(), "warp", UUID.fromString(AuroraMCAPI.getInfo().getName()).toString(), this.uuid.toString() + "\n" + target.getName());
                CommunicationUtils.sendMessage(protocolMessage);
            });
        }
    }
}
