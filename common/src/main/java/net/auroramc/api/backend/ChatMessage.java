/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.api.backend;

import net.auroramc.api.permissions.Rank;
import net.auroramc.api.player.ChatChannel;
import org.json.JSONObject;

import java.util.UUID;

public class ChatMessage {

    private final int amcId;
    private final String name;
    private final Rank rank;
    private final String message;
    private final long timestamp;
    private final boolean isDead;
    private final ChatChannel channel;
    private final int recipient;
    private final String recipientName;
    private final UUID partyUUID;

    public ChatMessage(int amcId, String name, Rank rank, String message, boolean isDead, ChatChannel chatChannel, int recipient, String recipientName, UUID partyUUID) {
        ChatMessage msg = this;
        this.amcId = amcId;
        this.name = name;
        this.rank = rank;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
        this.recipient = recipient;
        this.recipientName = recipientName;
        this.partyUUID = partyUUID;
        this.channel = chatChannel;
        this.isDead = isDead;
    }

    public String getName() {
        return name;
    }

    public int getAmcId() {
        return amcId;
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

    public boolean isDead() {
        return isDead;
    }

    public int getRecipient() {
        return recipient;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public UUID getPartyUUID() {
        return partyUUID;
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
        if (recipient != -1) {
            json.put("recipient", recipient);
            json.put("recipient_name", recipientName);
        }
        if (partyUUID != null) {
            json.put("partyUUID", partyUUID.toString());
        }
        return json;
    }
}
