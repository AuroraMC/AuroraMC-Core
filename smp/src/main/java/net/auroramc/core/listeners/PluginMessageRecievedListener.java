/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.core.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.Cosmetic;
import net.auroramc.api.cosmetics.FriendStatus;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.permissions.SubRank;
import net.auroramc.api.player.ChatChannel;
import net.auroramc.api.player.friends.FriendsList;
import net.auroramc.api.stats.Achievement;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.gui.friends.Friends;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.UUID;

public class PluginMessageRecievedListener implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String channel, Player player2, byte[] bytes) {
        if (channel.equals("auroramc:server")) {
            ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
            String subchannel = in.readUTF();
            switch (subchannel) {
                case "PlaySound": {
                    /*AuroraMCServerPlayer player = ServerAPI.getPlayer(in.readUTF());
                    String sound = in.readUTF();
                    int volume = in.readInt();
                    int pitch = in.readInt();
                    assert player != null;
                    player.playSound(player.getLocation(), Sound.valueOf(sound), volume, pitch);*/
                    break;
                }
                case "XPAdd": {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(in.readUTF());
                    assert player != null;
                    player.getStats().addXp(in.readLong(), false);
                    break;
                }
                case "XPRemove": {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(in.readUTF());
                    assert player != null;
                    player.getStats().removeXp(in.readLong(), false);
                    break;
                }
                case "StatisticIncrement": {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(in.readUTF());
                    assert player != null;
                    int gameId = in.readInt();
                    String key = in.readUTF();
                    long amount = in.readLong();
                    player.getStats().incrementStatistic(gameId, key, amount, false);
                    break;
                }
                case "AchievementGained": {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(in.readUTF());
                    assert player != null;
                    Achievement achievement = AuroraMCAPI.getAchievement(in.readInt());
                    int tier = in.readInt();
                    player.getStats().achievementGained(achievement, tier, false);
                    break;
                }
                case "AchievementRemoved": {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(in.readUTF());
                    assert player != null;
                    Achievement achievement = AuroraMCAPI.getAchievement(in.readInt());
                    player.getStats().achievementRemoved(achievement, false);
                    break;
                }
                case "AchievementProgress": {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(in.readUTF());
                    assert player != null;
                    Achievement achievement = AuroraMCAPI.getAchievement(in.readInt());
                    long amount = in.readLong();
                    player.getStats().addProgress(achievement, amount, player.getStats().getAchievementsGained().get(achievement), false);
                    break;
                }
                case "AchievementProgressTierGained": {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(in.readUTF());
                    assert player != null;
                    Achievement achievement = AuroraMCAPI.getAchievement(in.readInt());
                    long amount = in.readLong();
                    int tier = in.readInt();
                    player.getStats().addProgress(achievement, amount, player.getStats().getAchievementsGained().get(achievement), false);
                    break;
                }
                case "LevelAdd": {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(in.readUTF());
                    assert player != null;
                    player.getStats().addLevels(in.readInt(), false);
                    break;
                }
                case "LevelRemove": {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(in.readUTF());
                    assert player != null;
                    player.getStats().removeLevels(in.readInt(), false);
                    break;
                }
                case "CrownsEarned": {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(in.readUTF());
                    assert player != null;
                    player.getStats().addCrownsEarned(in.readLong(), false);
                    break;
                }
                case "TicketsEarned": {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(in.readUTF());
                    assert player != null;
                    player.getStats().addTicketsEarned(in.readLong(), false);
                    break;
                }
                case "TicketsAdded": {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(in.readUTF());
                    assert player != null;
                    long amount = in.readLong();
                    player.getBank().addTickets(amount, in.readBoolean(), false);
                    break;
                }
                case "CrownsAdded": {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(in.readUTF());
                    assert player != null;
                    long amount = in.readLong();
                    player.getBank().addCrowns(amount, in.readBoolean(), false);
                    break;
                }
                case "WithdrawCrowns": {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(in.readUTF());
                    assert player != null;
                    long amount = in.readLong();
                    player.getBank().withdrawCrowns(amount, in.readBoolean(), false);
                    break;
                }
                case "WithdrawTickets": {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(in.readUTF());
                    assert player != null;
                    long amount = in.readLong();
                    player.getBank().withdrawTickets(amount, in.readBoolean(), false);
                    break;
                }
                case "CrownsRemoved": {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(in.readUTF());
                    assert player != null;
                    long amount = in.readLong();
                    player.getStats().removeCrownsEarned(amount, false);
                    break;
                }
                case "TicketsRemoved": {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(in.readUTF());
                    assert player != null;
                    long amount = in.readLong();
                    player.getStats().removeTicketsEarned(amount, false);
                    break;
                }
                case "PlusSubscription": {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(in.readUTF());
                    assert player != null;
                    player.newSubscription();
                    break;
                }
                case "PlusExtend": {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(in.readUTF());
                    assert player != null;
                    if (player.getActiveSubscription() != null) {
                        player.getActiveSubscription().extend(in.readInt());
                    }
                    break;
                }
                case "FriendRequestAccepted": {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(in.readUTF());
                    assert player != null;
                    UUID uuid = UUID.fromString(in.readUTF());

                    boolean online = in.readBoolean();
                    String server = in.readUTF();
                    if (server.equals("null")) {
                        server = null;
                    }
                    FriendStatus status = (FriendStatus) AuroraMCAPI.getCosmetics().get(in.readInt());
                    player.getFriendsList().friendRequestAccepted(uuid, online, server, status, false);
                    if (ServerAPI.getGUI(player) != null) {
                        GUI gui = ServerAPI.getGUI(player);
                        if (gui instanceof Friends) {
                            Friends friends = (Friends) gui;
                            friends.reload();
                        }
                    }
                    break;
                }
                case "FriendRequestDenied": {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(in.readUTF());
                    assert player != null;
                    UUID uuid = UUID.fromString(in.readUTF());
                    player.getFriendsList().friendRequestRemoved(uuid, false, false);
                    if (ServerAPI.getGUI(player) != null) {
                        GUI gui = ServerAPI.getGUI(player);
                        if (gui instanceof Friends) {
                            Friends friends = (Friends) gui;
                            friends.reload();
                        }
                    }
                    break;
                }
                case "FriendDeleted": {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(in.readUTF());
                    assert player != null;
                    UUID uuid = UUID.fromString(in.readUTF());
                    player.getFriendsList().friendRemoved(uuid, false, false);
                    if (ServerAPI.getGUI(player) != null) {
                        GUI gui = ServerAPI.getGUI(player);
                        if (gui instanceof Friends) {
                            Friends friends = (Friends) gui;
                            friends.reload();
                        }
                    }
                    break;
                }
                case "FriendVisibilitySet": {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(in.readUTF());
                    assert player != null;
                    player.getFriendsList().setVisibilityMode(FriendsList.VisibilityMode.valueOf(in.readUTF()), false);
                    if (ServerAPI.getGUI(player) != null) {
                        GUI gui = ServerAPI.getGUI(player);
                        if (gui instanceof Friends) {
                            Friends friends = (Friends) gui;
                            friends.reload();
                        }
                    }
                    break;
                }
                case "FriendStatusSet": {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(in.readUTF());
                    assert player != null;
                    player.getFriendsList().setCurrentStatus((FriendStatus) AuroraMCAPI.getCosmetics().get(in.readInt()), false);
                    if (ServerAPI.getGUI(player) != null) {
                        GUI gui = ServerAPI.getGUI(player);
                        if (gui instanceof Friends) {
                            Friends friends = (Friends) gui;
                            friends.reload();
                        }
                    }
                    break;
                }
                case "FriendStatusUpdate": {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(in.readUTF());
                    assert player != null;
                    UUID uuid = UUID.fromString(in.readUTF());
                    player.getFriendsList().getFriends().get(uuid).setStatus((FriendStatus) AuroraMCAPI.getCosmetics().get(in.readInt()), false);
                    if (ServerAPI.getGUI(player) != null) {
                        GUI gui = ServerAPI.getGUI(player);
                        if (gui instanceof Friends) {
                            Friends friends = (Friends) gui;
                            friends.reload();
                        }
                    }
                    break;
                }
                case "FriendFavourited": {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(in.readUTF());
                    assert player != null;
                    player.getFriendsList().getFriends().get(UUID.fromString(in.readUTF())).favourited(false);
                    if (ServerAPI.getGUI(player) != null) {
                        GUI gui = ServerAPI.getGUI(player);
                        if (gui instanceof Friends) {
                            Friends friends = (Friends) gui;
                            friends.reload();
                        }
                    }
                    break;
                }
                case "FriendUnfavourited": {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(in.readUTF());
                    assert player != null;
                    player.getFriendsList().getFriends().get(UUID.fromString(in.readUTF())).unfavourited(false);
                    if (ServerAPI.getGUI(player) != null) {
                        GUI gui = ServerAPI.getGUI(player);
                        if (gui instanceof Friends) {
                            Friends friends = (Friends) gui;
                            friends.reload();
                        }
                    }
                    break;
                }
                case "FriendRequestAdded": {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(in.readUTF());
                    assert player != null;
                    UUID uuid = UUID.fromString(in.readUTF());
                    boolean outgoing = in.readBoolean();
                    String name = in.readUTF();
                    int amcId = in.readInt();
                    player.getFriendsList().newFriendRequest(uuid, name, amcId, outgoing, false);
                    if (ServerAPI.getGUI(player) != null) {
                        GUI gui = ServerAPI.getGUI(player);
                        if (gui instanceof Friends) {
                            Friends friends = (Friends) gui;
                            friends.reload();
                        }
                    }
                    break;
                }
                case "FriendLoggedOn": {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(in.readUTF());
                    assert player != null;
                    UUID uuid = UUID.fromString(in.readUTF());
                    String server = in.readUTF();
                    if (server.equals("null")) {
                        server = null;
                    }
                    FriendStatus status = (FriendStatus) AuroraMCAPI.getCosmetics().get(in.readInt());
                    if (player.isLoaded()) {
                        player.getFriendsList().getFriends().get(uuid).loggedOn(server, status, false);
                        if (ServerAPI.getGUI(player) != null) {
                            GUI gui = ServerAPI.getGUI(player);
                            if (gui instanceof Friends) {
                                Friends friends = (Friends) gui;
                                friends.reload();
                            }
                        }
                    }

                    break;
                }
                case "FriendLoggedOff": {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(in.readUTF());
                    assert player != null;
                    UUID uuid = UUID.fromString(in.readUTF());
                    if (player.isLoaded()) {
                        player.getFriendsList().getFriends().get(uuid).loggedOff(false);
                        if (ServerAPI.getGUI(player) != null) {
                            GUI gui = ServerAPI.getGUI(player);
                            if (gui instanceof Friends) {
                                Friends friends = (Friends) gui;
                                friends.reload();
                            }
                        }
                    }
                    break;
                }
                case "FriendServerUpdated": {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(in.readUTF());
                    if (player == null || !player.isLoaded()) {
                        return;
                    }
                    UUID uuid = UUID.fromString(in.readUTF());
                    String server = in.readUTF();
                    if (server.equals("null")) {
                        server = null;
                    }
                    player.getFriendsList().getFriends().get(uuid).updateServer(server, false);
                    if (ServerAPI.getGUI(player) != null) {
                        GUI gui = ServerAPI.getGUI(player);
                        if (gui instanceof Friends) {
                            Friends friends = (Friends) gui;
                            friends.reload();
                        }
                    }
                    break;
                }
                case "OpenFriendGUI": {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(in.readUTF());
                    assert player != null;
                    Friends friends = new Friends(player);
                    friends.open(player);
                    break;
                }
                case "ChatChannelSet": {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(in.readUTF());
                    assert player != null;
                    ChatChannel chatChannel = ChatChannel.valueOf(in.readUTF());
                    player.setChannel(chatChannel, false);
                    break;
                }
                case "PartySet": {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(in.readUTF());
                    assert player != null;
                    String uuidString = in.readUTF();
                    if (uuidString.equals("null")) {
                        player.setPartyLeader(null);
                    } else {
                        player.setPartyLeader(UUID.fromString(uuidString));
                    }
                    break;
                }
                case "CosmeticAdd": {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(in.readUTF());
                    assert player != null;
                    Cosmetic cosmetic = AuroraMCAPI.getCosmetics().get(in.readInt());
                    player.getUnlockedCosmetics().add(cosmetic);
                    break;
                }
                case "CosmeticRemove": {
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(in.readUTF());
                    assert player != null;
                    Cosmetic cosmetic = AuroraMCAPI.getCosmetics().get(in.readInt());
                    player.getUnlockedCosmetics().remove(cosmetic);
                    break;
                }
                case "SetRank": {
                    UUID uuid = UUID.fromString(in.readUTF());
                    Rank rank = Rank.getByID(in.readInt());
                    assert rank != null;
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(uuid);
                    if (player != null) {
                        player.setRank(rank);
                        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
                            AuroraMCServerPlayer otherAMCPlayer = ServerAPI.getPlayer(otherPlayer);
                            if (otherAMCPlayer != null) {
                                otherAMCPlayer.updateNametag(player);
                            }
                        }
                    }
                    break;
                }
                case "AddSubRank": {
                    UUID uuid = UUID.fromString(in.readUTF());
                    SubRank rank = SubRank.getByID(in.readInt());
                    assert rank != null;
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(uuid);
                    if (player != null) {
                        player.grantSubrank(rank);
                    }
                    break;
                }
                case "RemoveSubRank": {
                    UUID uuid = UUID.fromString(in.readUTF());
                    SubRank rank = SubRank.getByID(in.readInt());
                    assert rank != null;
                    AuroraMCServerPlayer player = ServerAPI.getPlayer(uuid);
                    if (player != null) {
                        player.revokeSubrank(rank);
                    }
                    break;
                }
            }
        }
    }
}
