package net.auroramc.core.api.backend;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.players.ChatChannel;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.json.JSONObject;

public class ChatMessage {

    private final int amcId;
    private final String name;
    private final Rank rank;
    private final String message;
    private final long timestamp;
    private final boolean isDead;
    private final BukkitTask expiryTask;
    private final ChatChannel channel;

    public ChatMessage(int amcId, String name, Rank rank, String message, boolean isDead, ChatChannel chatChannel) {
        ChatMessage msg = this;
        this.amcId = amcId;
        this.name = name;
        this.rank = rank;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
        this.isDead = isDead;
        this.expiryTask = new BukkitRunnable(){
            @Override
            public void run() {
                ChatLogs.removeChatMessage(msg);
            }
        }.runTaskLaterAsynchronously(AuroraMCAPI.getCore(), 30000);
        this.channel = chatChannel;
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

    public ChatChannel getChannel() {
        return channel;
    }

    public JSONObject toJson() {
        JSONObject json  = new JSONObject();
        json.put("amcId", amcId);
        json.put("name", name);
        json.put("rank", rank.getName());
        json.put("message", message);
        json.put("timestamp", timestamp);
        json.put("is_dead", isDead);
        json.put("channel", channel.name());
        return json;
    }
}
