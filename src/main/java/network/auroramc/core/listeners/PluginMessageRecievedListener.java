package network.auroramc.core.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.chat.TextComponent;
import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.permissions.Rank;
import network.auroramc.core.api.players.AuroraMCPlayer;
import network.auroramc.core.api.players.friends.Friend;
import network.auroramc.core.api.players.friends.FriendStatus;
import network.auroramc.core.api.players.friends.FriendsList;
import network.auroramc.core.api.stats.Achievement;
import network.auroramc.core.gui.friends.Friends;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.UUID;

public class PluginMessageRecievedListener implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String channel, Player player2, byte[] bytes) {
        if (channel.equals("Server")) {
            ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
            String subchannel = in.readUTF();
            if (subchannel.equals("PlaySound")) {
                switch (in.readUTF()) {
                    case "Message":
                        Player player = Bukkit.getPlayer(in.readUTF());
                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 2);
                        break;
                    default:
                        break;
                }
            } else if (subchannel.equalsIgnoreCase("DisguiseCheckFail")) {
                AuroraMCAPI.getPendingDisguiseChecks().remove(Bukkit.getPlayer(in.readUTF()));
            } else if (subchannel.equalsIgnoreCase("DisguiseCheckSuccess")) {
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(Bukkit.getPlayer(in.readUTF()));
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
            } else if (subchannel.equalsIgnoreCase("XPAdd")) {
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(Bukkit.getPlayer(in.readUTF()));
                player.getStats().addXp(in.readLong(), false);
            } else if (subchannel.equalsIgnoreCase("XPRemove")) {
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(Bukkit.getPlayer(in.readUTF()));
                player.getStats().removeXp(in.readLong());
            } else if (subchannel.equalsIgnoreCase("StatisticIncrement")) {
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(Bukkit.getPlayer(in.readUTF()));
                int gameId = in.readInt();
                String key = in.readUTF();
                long amount = in.readLong();
                player.getStats().incrementStatistic(gameId, key, amount, false);
            } else if (subchannel.equalsIgnoreCase("AchievementGained")) {
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(Bukkit.getPlayer(in.readUTF()));
                Achievement achievement = AuroraMCAPI.getAchievement(in.readInt());
                int tier = in.readInt();
                player.getStats().achievementGained(achievement, tier, false);
            } else if (subchannel.equalsIgnoreCase("AchievementRemoved")) {
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(Bukkit.getPlayer(in.readUTF()));
                Achievement achievement = AuroraMCAPI.getAchievement(in.readInt());
                player.getStats().achievementRemoved(achievement);
            } else if (subchannel.equalsIgnoreCase("AchievementProgress")) {
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(Bukkit.getPlayer(in.readUTF()));
                Achievement achievement = AuroraMCAPI.getAchievement(in.readInt());
                long amount = in.readLong();
                player.getStats().addProgress(achievement, amount, player.getStats().getAchievementsGained().get(achievement), false);
            } else if (subchannel.equalsIgnoreCase("AchievementProgressTierGained")) {
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(Bukkit.getPlayer(in.readUTF()));
                Achievement achievement = AuroraMCAPI.getAchievement(in.readInt());
                long amount = in.readLong();
                int tier = in.readInt();
                player.getStats().addProgress(achievement, amount, player.getStats().getAchievementsGained().get(achievement), false);
            } else if (subchannel.equalsIgnoreCase("LevelAdd")) {
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(Bukkit.getPlayer(in.readUTF()));
                player.getStats().addLevels(in.readInt());
            } else if (subchannel.equalsIgnoreCase("LevelRemove")) {
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(Bukkit.getPlayer(in.readUTF()));
                player.getStats().removeLevels(in.readInt());
            } else if (subchannel.equalsIgnoreCase("CrownsEarned")) {
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(Bukkit.getPlayer(in.readUTF()));
                player.getStats().addCrownsEarned(in.readLong(), false);
            } else if (subchannel.equalsIgnoreCase("TicketsEarned")) {
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(Bukkit.getPlayer(in.readUTF()));
                player.getStats().addTicketsEarned(in.readLong(), false);
            } else if (subchannel.equalsIgnoreCase("TicketsAdded")) {
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(Bukkit.getPlayer(in.readUTF()));
                long amount = in.readLong();
                player.getBank().addTickets(amount, in.readBoolean(), false);
            } else if (subchannel.equalsIgnoreCase("CrownsAdded")) {
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(Bukkit.getPlayer(in.readUTF()));
                long amount = in.readLong();
                player.getBank().addCrowns(amount, in.readBoolean(), false);
            } else if (subchannel.equalsIgnoreCase("WithdrawCrowns")) {
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(Bukkit.getPlayer(in.readUTF()));
                long amount = in.readLong();
                player.getBank().withdrawCrowns(amount, in.readBoolean(), false);
            } else if (subchannel.equalsIgnoreCase("WithdrawTickets")) {
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(Bukkit.getPlayer(in.readUTF()));
                long amount = in.readLong();
                player.getBank().withdrawTickets(amount, in.readBoolean(), false);
            } else if (subchannel.equalsIgnoreCase("CrownsRemoved")) {
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(Bukkit.getPlayer(in.readUTF()));
                long amount = in.readLong();
                player.getStats().removeCrownsEarned(amount, false);
            } else if (subchannel.equalsIgnoreCase("TicketsRemoved")) {
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(Bukkit.getPlayer(in.readUTF()));
                long amount = in.readLong();
                player.getStats().removeTicketsEarned(amount, false);
            } else if (subchannel.equalsIgnoreCase("PlusSubscription")) {
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(Bukkit.getPlayer(in.readUTF()));
                player.newSubscription();
            } else if (subchannel.equalsIgnoreCase("PlusExtend")) {
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(Bukkit.getPlayer(in.readUTF()));
                if (player.getActiveSubscription() != null) {
                    player.getActiveSubscription().extend(in.readInt());
                }
            } else if (subchannel.equalsIgnoreCase("FriendRequestAccepted")) {
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(Bukkit.getPlayer(in.readUTF()));
                UUID uuid = UUID.fromString(in.readUTF());

                boolean online = in.readBoolean();
                String server = in.readUTF();
                if (server.equals("null")) {
                    server = null;
                }
                FriendStatus status = FriendStatus.valueOf(in.readUTF());
                player.getFriendsList().friendRequestAccepted(uuid, online, server, status, false);
            } else if (subchannel.equalsIgnoreCase("FriendRequestDenied")) {
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(Bukkit.getPlayer(in.readUTF()));
                UUID uuid = UUID.fromString(in.readUTF());
                player.getFriendsList().friendRequestRemoved(uuid, false);
            } else if (subchannel.equalsIgnoreCase("FriendDeleted")) {
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(Bukkit.getPlayer(in.readUTF()));
                UUID uuid = UUID.fromString(in.readUTF());
                player.getFriendsList().friendRemoved(uuid, false);
            } else if (subchannel.equalsIgnoreCase("FriendVisibilitySet")) {
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(Bukkit.getPlayer(in.readUTF()));
                player.getFriendsList().setVisibilityMode(FriendsList.VisibilityMode.valueOf(in.readUTF()),false);
            } else if (subchannel.equalsIgnoreCase("FriendStatusSet")) {
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(Bukkit.getPlayer(in.readUTF()));
                player.getFriendsList().setCurrentStatus(FriendStatus.valueOf(in.readUTF()),false);
            } else if (subchannel.equalsIgnoreCase("FriendStatusUpdate")) {
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(Bukkit.getPlayer(in.readUTF()));
                UUID uuid = UUID.fromString(in.readUTF());
                player.getFriendsList().getFriends().get(uuid).setStatus(FriendStatus.valueOf(in.readUTF()));
            } else if (subchannel.equalsIgnoreCase("FriendFavourited")) {
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(Bukkit.getPlayer(in.readUTF()));
                player.getFriendsList().getFriends().get(UUID.fromString(in.readUTF())).favourited(false);
            } else if (subchannel.equalsIgnoreCase("FriendUnfavourited")) {
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(Bukkit.getPlayer(in.readUTF()));
                player.getFriendsList().getFriends().get(UUID.fromString(in.readUTF())).unfavourited(false);
            } else if (subchannel.equalsIgnoreCase("FriendRequestAdded")) {
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(Bukkit.getPlayer(in.readUTF()));
                UUID uuid = UUID.fromString(in.readUTF());
                boolean outgoing = in.readBoolean();
                String name = in.readUTF();
                int amcId = in.readInt();
                player.getFriendsList().newFriendRequest(uuid, name, amcId, outgoing);
            } else if (subchannel.equalsIgnoreCase("FriendLoggedOn")) {
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(Bukkit.getPlayer(in.readUTF()));
                UUID uuid = UUID.fromString(in.readUTF());
                String server = in.readUTF();
                if (server.equals("null")) {
                    server = null;
                }
                FriendStatus status = FriendStatus.valueOf(in.readUTF());
                player.getFriendsList().getFriends().get(uuid).loggedOn(server, status);
            } else if (subchannel.equalsIgnoreCase("FriendLoggedOff")) {
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(Bukkit.getPlayer(in.readUTF()));
                UUID uuid = UUID.fromString(in.readUTF());
                player.getFriendsList().getFriends().get(uuid).loggedOff();
            } else if (subchannel.equalsIgnoreCase("FriendServerUpdated")) {
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(Bukkit.getPlayer(in.readUTF()));
                UUID uuid = UUID.fromString(in.readUTF());
                String server = in.readUTF();
                if (server.equals("null")) {
                    server = null;
                }
                player.getFriendsList().getFriends().get(uuid).updateServer(server);
            } else if (subchannel.equalsIgnoreCase("OpenFriendGUI")) {
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(Bukkit.getPlayer(in.readUTF()));
                Friends friends = new Friends(player);
                friends.open(player);
                AuroraMCAPI.openGUI(player, friends);
            }
        }
    }
}
