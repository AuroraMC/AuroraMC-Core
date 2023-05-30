/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.api.player.friends;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.abstraction.ScheduleFactory;
import net.auroramc.api.cosmetics.FriendStatus;

import java.util.UUID;

public class Friend {

    public enum FriendType {NORMAL, FAVOURITE, PENDING_INCOMING, PENDING_OUTGOING}

    private FriendsList list;
    private final int amcId;
    private final UUID uuid;
    private final String name;
    private FriendType type;
    private boolean online;
    private String server;
    private FriendStatus status;

    public Friend(FriendsList list, int amcId, UUID uuid, String name, FriendType type, FriendStatus status) {
        this.list = list;
        this.amcId = amcId;
        this.uuid = uuid;
        this.name = name;
        this.type = type;
        this.status = status;
    }

    public void friendRequestAccepted(boolean online, String server, FriendStatus status) {
        this.type = FriendType.NORMAL;
        this.online = online;
        this.server = server;
        this.status = status;
    }

    public void loggedOn(String server, FriendStatus status, boolean send) {
        this.online = true;
        this.server = server;
        this.status = status;
        if (send) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("FriendLoggedOn");
            out.writeUTF(list.getPlayer().getName());
            out.writeUTF(uuid.toString());
            out.writeUTF(server + "");
            out.writeInt(status.getId());
            list.getPlayer().sendPluginMessage(out.toByteArray());
        }
    }

    public void loggedOff(boolean send) {
        this.online = false;
        this.server = null;
        this.status = (FriendStatus) AuroraMCAPI.getCosmetics().get(101);
        if (send) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("FriendLoggedOff");
            out.writeUTF(list.getPlayer().getName());
            out.writeUTF(uuid.toString());
            list.getPlayer().sendPluginMessage(out.toByteArray());
        }
    }

    public void favourited(boolean send) {
        this.type = FriendType.FAVOURITE;
        if (send) {
            ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().favouritedFriend(list.getPlayer(), uuid));
            if (!list.getPlayer().getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(33))) {
                list.getPlayer().getStats().achievementGained(AuroraMCAPI.getAchievement(33), 1, true);
            }
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("FriendFavourited");
            out.writeUTF(list.getPlayer().getName());
            out.writeUTF(uuid.toString());
            list.getPlayer().sendPluginMessage( out.toByteArray());
        }
    }

    public void unfavourited(boolean send) {
        this.type = FriendType.NORMAL;
        if (send) {
            ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().unfavouritedFriend(list.getPlayer(), uuid));
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("FriendUnfavourited");
            out.writeUTF(list.getPlayer().getName());
            out.writeUTF(uuid.toString());
            list.getPlayer().sendPluginMessage(out.toByteArray());
        }
    }

    public void updateServer(String server, boolean send) {
        this.server = server;
        if (send) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("FriendServerUpdated");
            out.writeUTF(list.getPlayer().getName());
            out.writeUTF(uuid.toString());
            out.writeUTF(server + "");
            list.getPlayer().sendPluginMessage(out.toByteArray());
        }
    }

    public boolean isOnline() {
        return online;
    }

    public FriendsList getList() {
        return list;
    }

    public FriendType getType() {
        return type;
    }

    public int getAmcId() {
        return amcId;
    }

    public String getName() {
        return name;
    }

    public String getServer() {
        return server;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setStatus(FriendStatus status, boolean send) {
        this.status = status;
        if (send) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("FriendStatusUpdate");
            out.writeUTF(list.getPlayer().getName());
            out.writeUTF(uuid.toString());
            out.writeInt(status.getId());
            list.getPlayer().sendPluginMessage(out.toByteArray());
        }
    }

    public FriendStatus getStatus() {
        if (status == null) {
            return (FriendStatus) AuroraMCAPI.getCosmetics().get(101);
        }
        return status;
    }

    public void setList(FriendsList list) {
        this.list = list;
    }
}
