/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.api.nova;

import net.auroramc.api.player.AuroraMCPlayer;

public class Violation {

    private final long timestamp;
    private final NovaCheck check;
    private final  AuroraMCPlayer player;

    public Violation(long timestamp, NovaCheck check, AuroraMCPlayer player) {
        this.timestamp = timestamp;
        this.check = check;
        this.player = player;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public AuroraMCPlayer getPlayer() {
        return player;
    }

    public NovaCheck getCheck() {
        return check;
    }
}
