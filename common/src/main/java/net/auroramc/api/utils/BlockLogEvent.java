/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.api.utils;

import java.util.UUID;

public class BlockLogEvent {

    private final long timestamp;
    private final LogType type;
    private final SMPLocation location;
    private final String material;
    private final UUID player;

    public BlockLogEvent(long timestamp, UUID player, LogType type, SMPLocation location, String material) {
        this.timestamp = timestamp;
        this.type = type;
        this.location = location;
        this.material = material;
        this.player = player;
    }

    public LogType getType() {
        return type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public SMPLocation getLocation() {
        return location;
    }

    public String getMaterial() {
        return material;
    }

    public UUID getPlayer() {
        return player;
    }

    public enum LogType{
        BREAK,
        PLACE,
        EXPLODE,
        FADE,
        FORM,
        BURN,
        FERTILIZE,
        SPREAD,
        DECAY,
        PRIME,
        ALL /* util for block log searches, do not ever use this to log. */


    }
}
