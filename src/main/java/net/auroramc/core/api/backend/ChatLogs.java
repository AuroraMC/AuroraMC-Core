package net.auroramc.core.api.backend;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.players.ChatChannel;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class ChatLogs {

    private static final List<ChatMessage> chatMessages;
    private static final Object lock;
    private static final Map<UUID, Integer> lastMessagesDumped;
    static {
        chatMessages = new ArrayList<>();
        lock = new Object();
        lastMessagesDumped = new HashMap<>();
    }

    public static void removeChatMessage(ChatMessage message) {
        synchronized (lock) {
            chatMessages.remove(message);
            for (Map.Entry<UUID, Integer> entry : new HashMap<>(lastMessagesDumped).entrySet()) {
                if (entry.getValue() == 0) {
                    lastMessagesDumped.remove(entry.getKey());
                } else {
                    lastMessagesDumped.put(entry.getKey(), entry.getValue() - 1);
                }
            }
        }
    }

    public static boolean appendMessages(UUID uuid) {
        synchronized (lock) {
            if (chatMessages.size() == lastMessagesDumped.getOrDefault(uuid, -1)) {
                return true;
            }

            JSONObject json = AuroraMCAPI.getDbManager().getChatLog(uuid);
            if (json == null) {
                return false;
            }

            JSONArray array = json.getJSONArray("log");
            for (ChatMessage message : chatMessages.subList(lastMessagesDumped.getOrDefault(uuid, -1) + 1, chatMessages.size())) {
                array.put(message.toJson());
            }

            if (AuroraMCAPI.getDbManager().updateChatlog(uuid, json)) {
                lastMessagesDumped.put(uuid, chatMessages.size() - 1);
                return true;
            } else {
                return false;
            }
        }
    }

    public static UUID reportDump() {
        synchronized (lock) {
            //Building JSON.
            JSONObject json = new JSONObject();
            json.put("server", AuroraMCAPI.getServerInfo().getName());
            UUID uuid = UUID.randomUUID();
            json.put("chatlog-uuid", uuid);
            long timestamp = System.currentTimeMillis();
            json.put("timestamp", timestamp);
            JSONArray array = new JSONArray();

            for (ChatMessage message : chatMessages) {
                array.put(message.toJson());
            }

            json.put("log", array);
            if (AuroraMCAPI.getDbManager().reportDump(uuid, json, timestamp)) {
                lastMessagesDumped.put(uuid, chatMessages.size() - 1);
                return uuid;
            } else {
                return null;
            }
        }
    }

    public static void chatMessage(int amcId, String name, Rank rank, String message, boolean isDead, ChatChannel channel) {
        synchronized (lock) {
            chatMessages.add(new ChatMessage(amcId, name, rank, message, isDead, channel));
        }
    }
}
