/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.api.stats;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.abstraction.ScheduleFactory;
import net.auroramc.api.player.AuroraMCPlayer;

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

    public void addTickets(long amount, boolean countTowardStats, boolean send) {
        tickets += amount;
        if (countTowardStats) {
            player.getStats().addTicketsEarned(amount, false);
        }

        if (send && !AuroraMCAPI.isTestServer()) {
            if (player != null) {
                ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().ticketsAdded(player, amount));
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("TicketsAdded");
                out.writeUTF(player.getName());
                out.writeLong(amount);
                out.writeBoolean(countTowardStats);
                player.sendPluginMessage(out.toByteArray());
            }
        }
    }

    public void addCrowns(long amount, boolean countTowardStats, boolean send) {
        crowns += amount;
        if (countTowardStats) {
            player.getStats().addCrownsEarned(amount, false);
        }

        if (send && !AuroraMCAPI.isTestServer()) {
            ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().crownsAdded(player, amount));
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("CrownsAdded");
            out.writeUTF(player.getName());
            out.writeLong(amount);
            out.writeBoolean(countTowardStats);
            player.sendPluginMessage(out.toByteArray());
        }
    }

    public boolean withdrawCrowns(long amount, boolean removeFromStats, boolean send) {
        if (amount > crowns) {
            return false;
        }
        crowns -= amount;
        if (removeFromStats) {
            player.getStats().removeCrownsEarned(amount, false);
        }

        if (send && !AuroraMCAPI.isTestServer()) {
            if (player != null) {
                ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().crownsAdded(player, -amount));
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("WithdrawCrowns");
                out.writeUTF(player.getName());
                out.writeLong(amount);
                out.writeBoolean(removeFromStats);
                player.sendPluginMessage(out.toByteArray());
            }
        }
        return true;
    }

    public boolean withdrawTickets(long amount, boolean removeFromStats, boolean send) {
        if (amount > tickets) {
            return false;
        }
        tickets -= amount;
        if (removeFromStats) {
            player.getStats().removeTicketsEarned(amount, false);
        }

        if (send && !AuroraMCAPI.isTestServer()) {
            if (player != null) {
                ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().ticketsAdded(player, -amount));
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("WithdrawTickets");
                out.writeUTF(player.getName());
                out.writeLong(amount);
                out.writeBoolean(removeFromStats);
                player.sendPluginMessage(out.toByteArray());
            }
        }
        return true;
    }
}
