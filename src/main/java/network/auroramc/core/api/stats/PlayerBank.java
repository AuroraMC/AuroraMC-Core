package network.auroramc.core.api.stats;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerBank {

    private final AuroraMCPlayer player;
    private long tickets;
    private long crowns;

    public PlayerBank(AuroraMCPlayer player, long tickets, long crowns) {
        this.player = player;
        this.tickets = tickets;
        this.crowns = crowns;
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

        if (player != null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    AuroraMCAPI.getDbManager().ticketsAdded(player, amount);
                }
            }.runTaskAsynchronously(AuroraMCAPI.getCore());
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

        if (player != null) {
            new BukkitRunnable(){
                @Override
                public void run() {
                    AuroraMCAPI.getDbManager().crownsAdded(player, amount);
                }
            }.runTaskAsynchronously(AuroraMCAPI.getCore());
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

        if (player != null) {
            new BukkitRunnable(){
                @Override
                public void run() {
                    AuroraMCAPI.getDbManager().crownsAdded(player, -amount);
                }
            }.runTaskAsynchronously(AuroraMCAPI.getCore());
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

        if (player != null) {
            new BukkitRunnable(){
                @Override
                public void run() {
                    AuroraMCAPI.getDbManager().ticketsAdded(player, -amount);
                }
            }.runTaskAsynchronously(AuroraMCAPI.getCore());
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
