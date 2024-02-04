/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.api.utils;

public class SMPPotionEffect {

    private final String type;
    private final int level, duration;

    public SMPPotionEffect(String type, int level, int duration) {
        this.type = type;
        this.level = level;
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public int getLevel() {
        return level;
    }

    public String getType() {
        return type;
    }
}
