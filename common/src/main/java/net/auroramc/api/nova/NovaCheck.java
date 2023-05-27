/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.api.nova;

public enum NovaCheck {

    XRAY_GOLD(35, 45, 64, "XRay (Gold)", 1800),
    XRAY_DIAMOND(35, 45, 64, "XRay (Diamond)", 1800),
    XRAY_EMERALD(35, 45, 64, "XRay (Emerald)", 1800),
    XRAY_NETHERITE(35, 45, 64, "XRay (Netherite)", 1800);

    private final int light, medium, severe;
    private final String name;
    private final long expirySeconds;

    NovaCheck(int light, int medium, int severe, String name, long expirySeconds) {
        this.light = light;
        this.medium = medium;
        this.severe = severe;
        this.name = name;
        this.expirySeconds = expirySeconds;
    }

    public String getName() {
        return name;
    }

    public int getLight() {
        return light;
    }

    public int getMedium() {
        return medium;
    }

    public int getSevere() {
        return severe;
    }

    public long getExpirySeconds() {
        return expirySeconds;
    }
}
