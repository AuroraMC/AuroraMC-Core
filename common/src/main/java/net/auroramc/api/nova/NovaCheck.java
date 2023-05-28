/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.api.nova;

public enum NovaCheck {

    XRAY_GOLD(25, 35, 55, 10, "XRay (Gold)", 36000),
    XRAY_DIAMOND(25, 35, 55, 10, "XRay (Diamond)", 36000),
    XRAY_EMERALD(25, 35, 55, 10, "XRay (Emerald)", 36000),
    XRAY_NETHERITE(25, 35, 55, 10, "XRay (Netherite)", 36000);

    private final int light, medium, severe, notificationFrequency;
    private final String name;
    private final long expiryTicks;

    NovaCheck(int light, int medium, int severe, int notificationFrequency, String name, long expiryTicks) {
        this.light = light;
        this.medium = medium;
        this.severe = severe;
        this.name = name;
        this.expiryTicks = expiryTicks;
        this.notificationFrequency = notificationFrequency;
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

    public long getExpiryTicks() {
        return expiryTicks;
    }

    public int getNotificationFrequency() {
        return notificationFrequency;
    }
}
