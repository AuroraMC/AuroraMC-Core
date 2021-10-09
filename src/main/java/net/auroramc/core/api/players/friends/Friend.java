/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.players.friends;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.cosmetics.FriendStatus;

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

    public void loggedOn(String server, FriendStatus status) {
        this.online = true;
        this.server = server;
        this.status = status;
    }

    public void loggedOff() {
        this.online = false;
        this.server = null;
        this.status = (FriendStatus) AuroraMCAPI.getCosmetics().get(101);
    }

    public void favourited(boolean sendToBungee) {
        this.type = FriendType.FAVOURITE;
        if (sendToBungee) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("FriendFavourited");
            out.writeUTF(list.getPlayer().getName());
            out.writeUTF(uuid.toString());
            list.getPlayer().getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
        }
    }

    public void unfavourited(boolean sendToBungee) {
        this.type = FriendType.NORMAL;
        if (sendToBungee) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("FriendUnfavourited");
            out.writeUTF(list.getPlayer().getName());
            out.writeUTF(uuid.toString());
            list.getPlayer().getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
        }
    }

    public void updateServer(String server) {
        this.server = server;
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

    public void setStatus(FriendStatus status) {
        this.status = status;
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
