/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.api.backend;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.player.ChatChannel;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

public class ChatLogs {

    private static final List<ChatMessage> chatMessages;
    private static final Object lock;
    private static final Map<UUID, Integer> lastMessagesDumped;
    private static final Map<UUID, UUID> partyUUIDs;

    static {
        chatMessages = new ArrayList<>();
        lock = new Object();
        lastMessagesDumped = new HashMap<>();
        partyUUIDs = new HashMap<>();
    }

    public static void removeChatMessage(ChatMessage message) {
        synchronized (lock) {
            chatMessages.remove(message);
            if (message.getPartyUUID() != null) {
                UUID uuid = partyUUIDs.get(message.getPartyUUID());
                if (uuid != null) {
                    if (lastMessagesDumped.get(uuid) != 0) {
                        lastMessagesDumped.put(uuid, lastMessagesDumped.get(uuid) - 1);
                    } else {
                        lastMessagesDumped.remove(uuid);
                        partyUUIDs.remove(message.getPartyUUID());
                    }
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

            String uuidString = null;
            UUID partyUUID;
            if (json.has("party_uuid")) {
                uuidString = json.getString("party_uuid");
                partyUUID = UUID.fromString(uuidString);
            } else {
                partyUUID = null;
            }

            JSONArray array = json.getJSONArray("log");
            if (partyUUID != null) {
                List<ChatMessage> messages = chatMessages.stream().filter(chatMessage -> chatMessage.getPartyUUID() != null).filter(chatMessage -> chatMessage.getPartyUUID().equals(partyUUID)).collect(Collectors.toList());
                for (ChatMessage message : messages.subList(lastMessagesDumped.getOrDefault(uuid, -1) + 1, chatMessages.size())) {
                    array.put(message.toJson());
                }
            } else{
                for (ChatMessage message : chatMessages.subList(lastMessagesDumped.getOrDefault(uuid, -1) + 1, chatMessages.size())) {
                    array.put(message.toJson());
                }
            }

            if (AuroraMCAPI.getDbManager().updateChatlog(uuid, json)) {
                lastMessagesDumped.put(uuid, chatMessages.size() - 1);
                return true;
            } else {
                return false;
            }
        }
    }

    public static UUID reportDump(int suspect, int recipient, UUID partyUUID) {
        synchronized (lock) {
            //Building JSON.
            JSONObject json = new JSONObject();
            json.put("server", AuroraMCAPI.getInfo().getName());
            UUID uuid = UUID.randomUUID();
            json.put("chatlog-uuid", uuid);
            long timestamp = System.currentTimeMillis();
            json.put("timestamp", timestamp);
            JSONArray array = new JSONArray();

            List<ChatMessage> messages = chatMessages;
            if (suspect != -1 && recipient != -1) {
                json.put("suspect", suspect);
                json.put("recipient", recipient);
                messages = messages.stream().filter(chatMessage -> (chatMessage.getAmcId() == suspect && chatMessage.getRecipient() == recipient) || (chatMessage.getAmcId() == recipient && chatMessage.getRecipient() == suspect)).collect(Collectors.toList());
            } else if (partyUUID != null) {
                json.put("party_uuid", partyUUID.toString());
                messages = messages.stream().filter(chatMessage -> chatMessage.getPartyUUID() != null).filter(chatMessage -> chatMessage.getPartyUUID().equals(partyUUID)).collect(Collectors.toList());
            }

            for (ChatMessage message : messages) {
                array.put(message.toJson());
            }

            json.put("log", array);
            if (AuroraMCAPI.getDbManager().reportDump(uuid, json, timestamp)) {
                lastMessagesDumped.put(uuid, messages.size() - 1);
                if (partyUUID != null) {
                    partyUUIDs.put(partyUUID, uuid);
                }
                return uuid;
            } else {
                return null;
            }
        }
    }

    public static void chatMessage(int amcId, String name, Rank rank, String message, boolean isDead, ChatChannel channel, int recipient, String recipientName, UUID partyUUID) {
        synchronized (lock) {
            chatMessages.add(new ChatMessage(amcId, name, rank, message, isDead, channel, recipient, recipientName, partyUUID));
        }
    }

    public static boolean hasChatted(int player) {
        for (ChatMessage message : chatMessages) {
            if (message.getAmcId() == player) {
                return true;
            }
        }
        return false;
    }
}
