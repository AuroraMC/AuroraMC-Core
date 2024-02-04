/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.api.backend.bigbrother;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.UUID;

public class WatchdogException {
    private final long timestamp;
    private final UUID uuid;
    private final String name;
    private final String trace;
    private final String commandSyntax;
    private final UUID player;
    private final boolean proxy;
    private final String server;
    private final JSONObject serverState;
    private final JSONArray otherOccurrences;

    public WatchdogException(long timestamp, UUID uuid, String name, String trace, String commandSyntax, UUID player, boolean proxy, String server, JSONObject serverState, JSONArray otherOccurrences) {
        this.timestamp = timestamp;
        this.uuid = uuid;
        this.name = name;
        this.trace = trace;
        this.commandSyntax = commandSyntax;

        this.player = player;
        this.proxy = proxy;
        this.server = server;
        this.serverState = serverState;
        this.otherOccurrences = otherOccurrences;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getTrace() {
        return trace;
    }

    public String getCommandSyntax() {
        return commandSyntax;
    }

    public UUID getPlayer() {
        return player;
    }

    public boolean isProxy() {
        return proxy;
    }

    public String getServer() {
        return server;
    }

    public JSONObject getServerState() {
        return serverState;
    }

    public JSONArray getOtherOccurrences() {
        return otherOccurrences;
    }
}
