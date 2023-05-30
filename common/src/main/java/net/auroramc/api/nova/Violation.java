/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
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
