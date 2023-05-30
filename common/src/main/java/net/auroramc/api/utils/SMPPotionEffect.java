/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
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
