package net.auroramc.core.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.ChatChannel;
import net.auroramc.core.api.players.friends.FriendStatus;
import net.auroramc.core.api.players.friends.FriendsList;
import net.auroramc.core.api.stats.Achievement;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.gui.friends.Friends;
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
                case "DisguiseCheckFail":
                    AuroraMCAPI.getPendingDisguiseChecks().remove(Objects.requireNonNull(AuroraMCAPI.getPlayer(in.readUTF())).getPlayer());
                    break;
                case "DisguiseCheckSuccess": {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(in.readUTF());
                    assert player != null;
                    String[] disguise = AuroraMCAPI.getPendingDisguiseChecks().get(player.getPlayer()).split(";");
                    Rank chosenRank = AuroraMCAPI.getRanks().get(Integer.parseInt(disguise[2]));
                    if (player.disguise(disguise[0], disguise[1], chosenRank)) {
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("Disguise");
                        out.writeUTF(player.getName());
                        out.writeUTF(disguise[0]);
                        out.writeUTF(disguise[1]);
                        out.writeInt(chosenRank.getId());
                        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
                        switch (in.readInt()) {
                            case 1:
                                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", String.format("You are now disguised as **%s**. To undisguise, simply type **/undisguise**.", disguise[0])));
                                break;
                            case 2:
                                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", String.format("You are now disguised as **%s** with the skin of **%s**. To undisguise, simply type **/undisguise**.", disguise[1], disguise[0])));
                                break;
                        }
                    } else {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "Something went wrong when attempting to disguise. Maybe the disguise skin doesn't exist?"));
                    }
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
                    FriendStatus status = FriendStatus.valueOf(in.readUTF());
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
                    player.getFriendsList().setCurrentStatus(FriendStatus.valueOf(in.readUTF()), false);
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
                    player.getFriendsList().getFriends().get(uuid).setStatus(FriendStatus.valueOf(in.readUTF()));
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
                    FriendStatus status = FriendStatus.valueOf(in.readUTF());
                    player.getFriendsList().getFriends().get(uuid).loggedOn(server, status);
                    if (AuroraMCAPI.getGUI(player) != null) {
                        GUI gui = AuroraMCAPI.getGUI(player);
                        if (gui instanceof Friends) {
                            Friends friends = (Friends) gui;
                            friends.reload();
                        }
                    }
                    break;
                }
                case "FriendLoggedOff": {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(in.readUTF());
                    assert player != null;
                    UUID uuid = UUID.fromString(in.readUTF());
                    player.getFriendsList().getFriends().get(uuid).loggedOff();
                    if (AuroraMCAPI.getGUI(player) != null) {
                        GUI gui = AuroraMCAPI.getGUI(player);
                        if (gui instanceof Friends) {
                            Friends friends = (Friends) gui;
                            friends.reload();
                        }
                    }
                    break;
                }
                case "FriendServerUpdated": {
                    AuroraMCPlayer player = AuroraMCAPI.getPlayer(in.readUTF());
                    assert player != null;
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
            }
        }
    }
}
