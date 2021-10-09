/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.players.lookup;

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
