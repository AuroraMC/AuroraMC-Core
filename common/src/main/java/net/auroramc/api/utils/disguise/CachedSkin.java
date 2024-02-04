/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.api.utils.disguise;

public class CachedSkin extends Skin {

    private final long lastUpdated;

    public CachedSkin(String value, String signature, long lastUpdated) {
        super(value, signature);
        this.lastUpdated = lastUpdated;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }
}
