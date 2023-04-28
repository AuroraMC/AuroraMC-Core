/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.api.player.friends;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.abstraction.ScheduleFactory;
import net.auroramc.api.cosmetics.FriendStatus;
import net.auroramc.api.player.AuroraMCPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FriendsList {

    public enum VisibilityMode {ALL, FAVOURITE_FRIENDS_ONLY, NOBODY}

    private AuroraMCPlayer player;
    private final Map<UUID, Friend> friends;
    private final Map<UUID, Friend> pendingFriendRequests;
    private VisibilityMode visibilityMode;
    private FriendStatus currentStatus;

    public FriendsList(AuroraMCPlayer player, Map<UUID, Friend> friends, Map<UUID, Friend> pendingFriendRequests, VisibilityMode visibilityMode, FriendStatus status) {
        this.player = player;
        this.friends = friends;
        for (Friend friend : this.friends.values()) {
            friend.setList(this);
        }
        this.pendingFriendRequests = pendingFriendRequests;
        for (Friend friend : this.pendingFriendRequests.values()) {
            friend.setList(this);
        }
        this.visibilityMode = visibilityMode;
        this.currentStatus = status;
    }

    public void setPlayer(AuroraMCPlayer player) {
        this.player = player;
    }

    public void newFriendRequest(UUID uuid, String name, int amcId, boolean outgoing, boolean send) {
        pendingFriendRequests.put(uuid, new Friend(this, amcId, uuid, name, ((outgoing)?Friend.FriendType.PENDING_OUTGOING:Friend.FriendType.PENDING_INCOMING), null));
        if (send) {
            if (outgoing) {
                ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().newFriendRequest(player, uuid));
            }
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("FriendRequestAdded");
            out.writeUTF(player.getName());
            out.writeUTF(uuid.toString());
            out.writeBoolean(outgoing);
            out.writeUTF(name);
            out.writeInt(amcId);
            player.sendPluginMessage(out.toByteArray());
        }
    }

    public void friendRequestAccepted(UUID uuid, boolean online, String server, FriendStatus status, boolean send) {
        if (send) {
            Friend friend = pendingFriendRequests.remove(uuid);
            friend.friendRequestAccepted(online, server, status);
            friends.put(uuid, friend);
            if (friend.getType() == Friend.FriendType.PENDING_INCOMING) {
                ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().friendRequestAccepted(player, uuid));
            }
            if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(30))) {
                player.getStats().achievementGained(AuroraMCAPI.getAchievement(30), 1, true);
            }
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("FriendRequestAccepted");
            out.writeUTF(player.getName());
            out.writeUTF(uuid.toString());
            out.writeBoolean(online);
            out.writeUTF(server + "");
            out.writeInt(status.getId());
            player.sendPluginMessage(out.toByteArray());
        } else {
            Friend friend = pendingFriendRequests.remove(uuid);
            friend.friendRequestAccepted(online, server, status);
            friends.put(uuid, friend);
        }
    }

    public void friendRequestRemoved(UUID uuid, boolean send, boolean db) {
        if (db) {
            ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().friendDeleted(player, uuid));
        }
        pendingFriendRequests.remove(uuid);
        if (send) {
            if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(32))) {
                player.getStats().achievementGained(AuroraMCAPI.getAchievement(32), 1, true);
            }
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("FriendRequestDenied");
            out.writeUTF(player.getName());
            out.writeUTF(uuid.toString());
            player.sendPluginMessage(out.toByteArray());
        }
    }

    public void friendRemoved(UUID uuid, boolean send, boolean db) {
        friends.remove(uuid);
        pendingFriendRequests.remove(uuid);
        if (db) {
            ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().friendDeleted(player, uuid));
        }
        if (send) {
            if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(31))) {
                player.getStats().achievementGained(AuroraMCAPI.getAchievement(31), 1, true);
            }
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("FriendDeleted");
            out.writeUTF(player.getName());
            out.writeUTF(uuid.toString());
            player.sendPluginMessage(out.toByteArray());
        }
    }

    public AuroraMCPlayer getPlayer() {
        return player;
    }

    public Map<UUID, Friend> getFriends() {
        return new HashMap<>(friends);
    }

    public Map<UUID, Friend> getPendingFriendRequests() {
        return new HashMap<>(pendingFriendRequests);
    }

    public VisibilityMode getVisibilityMode() {
        return visibilityMode;
    }

    public void setVisibilityMode(VisibilityMode visibilityMode, boolean send) {
        this.visibilityMode = visibilityMode;
        if (send) {
            ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().visibilitySet(player, visibilityMode));
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("FriendVisibilitySet");
            out.writeUTF(player.getName());
            out.writeUTF(visibilityMode.name());
            player.sendPluginMessage(out.toByteArray());
        }
    }

    public FriendStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(FriendStatus currentStatus, boolean send) {
        this.currentStatus = currentStatus;
        if (send) {
            ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().statusSet(player, currentStatus));
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("FriendStatusSet");
            out.writeUTF(player.getName());
            out.writeInt(currentStatus.getId());
            player.sendPluginMessage(out.toByteArray());
        }
    }
}
