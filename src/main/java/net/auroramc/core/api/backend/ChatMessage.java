package net.auroramc.core.api.backend;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.Rank;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class ChatMessage {

    private final int amcId;
    private final String name;
    private final Rank rank;
    private final String message;
    private final long timestamp;
    private final BukkitTask expiryTask;

    public ChatMessage(int amcId, String name, Rank rank, String message) {
        this.amcId = amcId;
        this.name = name;
        this.rank = rank;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
        this.expiryTask = new BukkitRunnable(){
            @Override
            public void run() {

            }
        }.runTaskLater(AuroraMCAPI.getCore(), 720000);
    }

    public String getName() {
        return name;
    }

    public int getAmcId() {
        return amcId;
    }

    public BukkitTask getExpiryTask() {
        return expiryTask;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Rank getRank() {
        return rank;
    }

    public String getMessage() {
        return message;
    }
}
