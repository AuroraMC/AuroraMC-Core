/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.cosmetics.Cosmetic;
import net.auroramc.core.api.cosmetics.FriendStatus;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.permissions.SubRank;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.ChatChannel;
import net.auroramc.core.api.players.friends.FriendsList;
import net.auroramc.core.api.stats.Achievement;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.gui.friends.Friends;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.Objects;
import java.util.UUID;

public class PluginMessageRecievedListener implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String channel, Player player2, byte[] bytes) {
        if (channel.equals("auroramc:server")) {
            ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
            String subchannel = in.readUTF();
            switch (subchannel) {
                case "PlaySound": {
                    Player player = Objects.requireNonNull(AuroraMCAPI.getPlayer(in.readUTF())).getPlayer();
                    String sound = in.readUTF();
                    int volume = in.readInt();
                    int pitch = in.readInt();
                    player.playSound(player.getLocation(), Sound.valueOf(sound), volume, pitch);
                    break;
                }
                case "XPAdd": {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(in.readUTF());
                    assert player != null;
                    player.getStats().addXp(in.readLong(), false);
                    break;
                }
                case "XPRemove": {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(in.readUTF());
                    assert player != null;
                    player.getStats().removeXp(in.readLong());
                    break;
                }
                case "StatisticIncrement": {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(in.readUTF());
                    assert player != null;
                    int gameId = in.readInt();
                    String key = in.readUTF();
                    long amount = in.readLong();
                    player.getStats().incrementStatistic(gameId, key, amount, false);
                    break;
                }
                case "AchievementGained": {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(in.readUTF());
                    assert player != null;
                    Achievement achievement = AuroraMCAPI.getAchievement(in.readInt());
                    int tier = in.readInt();
                    player.getStats().achievementGained(achievement, tier, false);
                    break;
                }
                case "AchievementRemoved": {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(in.readUTF());
                    assert player != null;
                    Achievement achievement = AuroraMCAPI.getAchievement(in.readInt());
                    player.getStats().achievementRemoved(achievement);
                    break;
                }
                case "AchievementProgress": {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(in.readUTF());
                    assert player != null;
                    Achievement achievement = AuroraMCAPI.getAchievement(in.readInt());
                    long amount = in.readLong();
                    player.getStats().addProgress(achievement, amount, player.getStats().getAchievementsGained().get(achievement), false);
                    break;
                }
                case "AchievementProgressTierGained": {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(in.readUTF());
                    assert player != null;
                    Achievement achievement = AuroraMCAPI.getAchievement(in.readInt());
                    long amount = in.readLong();
                    int tier = in.readInt();
                    player.getStats().addProgress(achievement, amount, player.getStats().getAchievementsGained().get(achievement), false);
                    break;
                }
                case "LevelAdd": {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(in.readUTF());
                    assert player != null;
                    player.getStats().addLevels(in.readInt());
                    break;
                }
                case "LevelRemove": {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(in.readUTF());
                    assert player != null;
                    player.getStats().removeLevels(in.readInt());
                    break;
                }
                case "CrownsEarned": {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(in.readUTF());
                    assert player != null;
                    player.getStats().addCrownsEarned(in.readLong(), false);
                    break;
                }
                case "TicketsEarned": {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(in.readUTF());
                    assert player != null;
                    player.getStats().addTicketsEarned(in.readLong(), false);
                    break;
                }
                case "TicketsAdded": {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(in.readUTF());
                    assert player != null;
                    long amount = in.readLong();
                    player.getBank().addTickets(amount, in.readBoolean(), false);
                    break;
                }
                case "CrownsAdded": {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(in.readUTF());
                    assert player != null;
                    long amount = in.readLong();
                    player.getBank().addCrowns(amount, in.readBoolean(), false);
                    break;
                }
                case "WithdrawCrowns": {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(in.readUTF());
                    assert player != null;
                    long amount = in.readLong();
                    player.getBank().withdrawCrowns(amount, in.readBoolean(), false);
                    break;
                }
                case "WithdrawTickets": {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(in.readUTF());
                    assert player != null;
                    long amount = in.readLong();
                    player.getBank().withdrawTickets(amount, in.readBoolean(), false);
                    break;
                }
                case "CrownsRemoved": {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(in.readUTF());
                    assert player != null;
                    long amount = in.readLong();
                    player.getStats().removeCrownsEarned(amount, false);
                    break;
                }
                case "TicketsRemoved": {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(in.readUTF());
                    assert player != null;
                    long amount = in.readLong();
                    player.getStats().removeTicketsEarned(amount, false);
                    break;
                }
                case "PlusSubscription": {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(in.readUTF());
                    assert player != null;
                    player.newSubscription();
                    break;
                }
                case "PlusExtend": {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(in.readUTF());
                    assert player != null;
                    if (player.getActiveSubscription() != null) {
                        player.getActiveSubscription().extend(in.readInt());
                    }
                    break;
                }
                case "FriendRequestAccepted": {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(in.readUTF());
                    assert player != null;
                    UUID uuid = UUID.fromString(in.readUTF());

                    boolean online = in.readBoolean();
                    String server = in.readUTF();
                    if (server.equals("null")) {
                        server = null;
                    }
                    FriendStatus status = (FriendStatus) AuroraMCAPI.getCosmetics().get(in.readInt());
                    player.getFriendsList().friendRequestAccepted(uuid, online, server, status, false);
                    if (AuroraMCAPI.getGUI(player) != null) {
                        GUI gui = AuroraMCAPI.getGUI(player);
                        if (gui instanceof Friends) {
                            Friends friends = (Friends) gui;
                            friends.reload();
                        }
                    }
                    break;
                }
                case "FriendRequestDenied": {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(in.readUTF());
                    assert player != null;
                    UUID uuid = UUID.fromString(in.readUTF());
                    player.getFriendsList().friendRequestRemoved(uuid, false);
                    if (AuroraMCAPI.getGUI(player) != null) {
                        GUI gui = AuroraMCAPI.getGUI(player);
                        if (gui instanceof Friends) {
                            Friends friends = (Friends) gui;
                            friends.reload();
                        }
                    }
                    break;
                }
                case "FriendDeleted": {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(in.readUTF());
                    assert player != null;
                    UUID uuid = UUID.fromString(in.readUTF());
                    player.getFriendsList().friendRemoved(uuid, false);
                    if (AuroraMCAPI.getGUI(player) != null) {
                        GUI gui = AuroraMCAPI.getGUI(player);
                        if (gui instanceof Friends) {
                            Friends friends = (Friends) gui;
                            friends.reload();
                        }
                    }
                    break;
                }
                case "FriendVisibilitySet": {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(in.readUTF());
                    assert player != null;
                    player.getFriendsList().setVisibilityMode(FriendsList.VisibilityMode.valueOf(in.readUTF()), false);
                    if (AuroraMCAPI.getGUI(player) != null) {
                        GUI gui = AuroraMCAPI.getGUI(player);
                        if (gui instanceof Friends) {
                            Friends friends = (Friends) gui;
                            friends.reload();
                        }
                    }
                    break;
                }
                case "FriendStatusSet": {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(in.readUTF());
                    assert player != null;
                    player.getFriendsList().setCurrentStatus((FriendStatus) AuroraMCAPI.getCosmetics().get(in.readInt()), false);
                    if (AuroraMCAPI.getGUI(player) != null) {
                        GUI gui = AuroraMCAPI.getGUI(player);
                        if (gui instanceof Friends) {
                            Friends friends = (Friends) gui;
                            friends.reload();
                        }
                    }
                    break;
                }
                case "FriendStatusUpdate": {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(in.readUTF());
                    assert player != null;
                    UUID uuid = UUID.fromString(in.readUTF());
                    player.getFriendsList().getFriends().get(uuid).setStatus((FriendStatus) AuroraMCAPI.getCosmetics().get(in.readInt()));
                    if (AuroraMCAPI.getGUI(player) != null) {
                        GUI gui = AuroraMCAPI.getGUI(player);
                        if (gui instanceof Friends) {
                            Friends friends = (Friends) gui;
                            friends.reload();
                        }
                    }
                    break;
                }
                case "FriendFavourited": {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(in.readUTF());
                    assert player != null;
                    player.getFriendsList().getFriends().get(UUID.fromString(in.readUTF())).favourited(false);
                    if (AuroraMCAPI.getGUI(player) != null) {
                        GUI gui = AuroraMCAPI.getGUI(player);
                        if (gui instanceof Friends) {
                            Friends friends = (Friends) gui;
                            friends.reload();
                        }
                    }
                    break;
                }
                case "FriendUnfavourited": {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(in.readUTF());
                    assert player != null;
                    player.getFriendsList().getFriends().get(UUID.fromString(in.readUTF())).unfavourited(false);
                    if (AuroraMCAPI.getGUI(player) != null) {
                        GUI gui = AuroraMCAPI.getGUI(player);
                        if (gui instanceof Friends) {
                            Friends friends = (Friends) gui;
                            friends.reload();
                        }
                    }
                    break;
                }
                case "FriendRequestAdded": {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(in.readUTF());
                    assert player != null;
                    UUID uuid = UUID.fromString(in.readUTF());
                    boolean outgoing = in.readBoolean();
                    String name = in.readUTF();
                    int amcId = in.readInt();
                    player.getFriendsList().newFriendRequest(uuid, name, amcId, outgoing);
                    if (AuroraMCAPI.getGUI(player) != null) {
                        GUI gui = AuroraMCAPI.getGUI(player);
                        if (gui instanceof Friends) {
                            Friends friends = (Friends) gui;
                            friends.reload();
                        }
                    }
                    break;
                }
                case "FriendLoggedOn": {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(in.readUTF());
                    assert player != null;
                    UUID uuid = UUID.fromString(in.readUTF());
                    String server = in.readUTF();
                    if (server.equals("null")) {
                        server = null;
                    }
                    FriendStatus status = (FriendStatus) AuroraMCAPI.getCosmetics().get(in.readInt());
                    if (player.isLoaded()) {
                        player.getFriendsList().getFriends().get(uuid).loggedOn(server, status);
                        if (AuroraMCAPI.getGUI(player) != null) {
                            GUI gui = AuroraMCAPI.getGUI(player);
                            if (gui instanceof Friends) {
                                Friends friends = (Friends) gui;
                                friends.reload();
                            }
                        }
                    }

                    break;
                }
                case "FriendLoggedOff": {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(in.readUTF());
                    assert player != null;
                    UUID uuid = UUID.fromString(in.readUTF());
                    if (player.isLoaded()) {
                        player.getFriendsList().getFriends().get(uuid).loggedOff();
                        if (AuroraMCAPI.getGUI(player) != null) {
                            GUI gui = AuroraMCAPI.getGUI(player);
                            if (gui instanceof Friends) {
                                Friends friends = (Friends) gui;
                                friends.reload();
                            }
                        }
                    }
                    break;
                }
                case "FriendServerUpdated": {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(in.readUTF());
                    if (player == null || !player.isLoaded()) {
                        return;
                    }
                    UUID uuid = UUID.fromString(in.readUTF());
                    String server = in.readUTF();
                    if (server.equals("null")) {
                        server = null;
                    }
                    player.getFriendsList().getFriends().get(uuid).updateServer(server);
                    if (AuroraMCAPI.getGUI(player) != null) {
                        GUI gui = AuroraMCAPI.getGUI(player);
                        if (gui instanceof Friends) {
                            Friends friends = (Friends) gui;
                            friends.reload();
                        }
                    }
                    break;
                }
                case "OpenFriendGUI": {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(in.readUTF());
                    assert player != null;
                    Friends friends = new Friends(player);
                    friends.open(player);
                    AuroraMCAPI.openGUI(player, friends);
                    break;
                }
                case "ChatChannelSet": {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(in.readUTF());
                    assert player != null;
                    ChatChannel chatChannel = ChatChannel.valueOf(in.readUTF());
                    player.setChannel(chatChannel);
                    break;
                }
                case "PartySet": {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(in.readUTF());
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
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(in.readUTF());
                    assert player != null;
                    Cosmetic cosmetic = AuroraMCAPI.getCosmetics().get(in.readInt());
                    player.getUnlockedCosmetics().add(cosmetic);
                    break;
                }
                case "CosmeticRemove": {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(in.readUTF());
                    assert player != null;
                    Cosmetic cosmetic = AuroraMCAPI.getCosmetics().get(in.readInt());
                    player.getUnlockedCosmetics().remove(cosmetic);
                    break;
                }
                case "SetRank": {
                    UUID uuid = UUID.fromString(in.readUTF());
                    Rank rank = Rank.getByID(in.readInt());
                    assert rank != null;
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(uuid);
                    if (player != null) {
                        player.setRank(rank);
                        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
                            AuroraMCPlayer otherAMCPlayer = AuroraMCAPI.getPlayer(otherPlayer);
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
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(uuid);
                    if (player != null) {
                        player.grantSubrank(rank);
                    }
                    break;
                }
                case "RemoveSubRank": {
                    UUID uuid = UUID.fromString(in.readUTF());
                    SubRank rank = SubRank.getByID(in.readInt());
                    assert rank != null;
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(uuid);
                    if (player != null) {
                        player.revokeSubrank(rank);
                    }
                    break;
                }
            }
        }
    }
}
