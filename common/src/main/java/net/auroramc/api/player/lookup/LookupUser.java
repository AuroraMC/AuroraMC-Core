/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.api.player.lookup;

public class LookupUser {

    private final int id;
    private final String name;
    private final boolean muted;
    private final boolean banned;

    public LookupUser(int id, String name, boolean muted, boolean banned) {
        this.id = id;
        this.name = name;
        this.muted = muted;
        this.banned = banned;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isBanned() {
        return banned;
    }

    public boolean isMuted() {
        return muted;
    }
}
