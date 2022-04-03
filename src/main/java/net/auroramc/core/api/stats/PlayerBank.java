/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.stats;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;

public class PlayerBank {

    private AuroraMCPlayer player;
    private long tickets;
    private long crowns;

    public PlayerBank(AuroraMCPlayer player, long tickets, long crowns) {
        this.player = player;
        this.tickets = tickets;
        this.crowns = crowns;
    }

    public void setPlayer(AuroraMCPlayer player) {
        this.player = player;
    }

    public AuroraMCPlayer getPlayer() {
        return player;
    }

    public long getCrowns() {
        return crowns;
    }

    public long getTickets() {
        return tickets;
    }

    public void addTickets(long amount, boolean countTowardStats, boolean sendToServer) {
        tickets += amount;
        if (countTowardStats) {
            player.getStats().addTicketsEarned(amount, false);
        }

        if (sendToServer) {
            if (player != null) {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("TicketsAdded");
                out.writeUTF(player.getName());
                out.writeLong(amount);
                out.writeBoolean(countTowardStats);
                player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(),"BungeeCord", out.toByteArray());
            }
        }
    }

    public void addCrowns(long amount, boolean countTowardStats, boolean sendToServer) {
        crowns += amount;
        if (countTowardStats) {
            player.getStats().addCrownsEarned(amount, false);
        }

        if (sendToServer) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("CrownsAdded");
            out.writeUTF(player.getName());
            out.writeLong(amount);
            out.writeBoolean(countTowardStats);
            player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(),"BungeeCord", out.toByteArray());
        }
    }

    public boolean withdrawCrowns(long amount, boolean removeFromStats, boolean sendToServer) {
        if (amount > crowns) {
            return false;
        }
        crowns -= amount;
        if (removeFromStats) {
            player.getStats().removeCrownsEarned(amount, false);
        }

        if (sendToServer) {
            if (player != null) {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("WithdrawCrowns");
                out.writeUTF(player.getName());
                out.writeLong(amount);
                out.writeBoolean(removeFromStats);
                player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(),"BungeeCord", out.toByteArray());
            }
        }
        return true;
    }

    public boolean withdrawTickets(long amount, boolean removeFromStats, boolean sendToServer) {
        if (amount > tickets) {
            return false;
        }
        tickets -= amount;
        if (removeFromStats) {
            player.getStats().removeTicketsEarned(amount, false);
        }

        if (sendToServer) {
            if (player != null) {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("WithdrawTickets");
                out.writeUTF(player.getName());
                out.writeLong(amount);
                out.writeBoolean(removeFromStats);
                player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(),"BungeeCord", out.toByteArray());
            }
        }
        return true;
    }
}
