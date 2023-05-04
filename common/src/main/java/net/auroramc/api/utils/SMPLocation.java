/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.api.utils;

public class SMPLocation {

    private final Dimension dimension;
    private final double x, y, z;
    private final float pitch, yaw;
    private final Reason reason;

    public SMPLocation(Dimension dimension, double x, double y, double z, float pitch, float yaw, Reason reason) {
        this.dimension = dimension;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
        this.reason = reason;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public float getPitch() {
        return pitch;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public float getYaw() {
        return yaw;
    }

    public double getZ() {
        return z;
    }

    public Reason getReason() {
        return reason;
    }

    public enum Dimension {
        OVERWORLD,
        NETHER,
        END;
    }

    public enum Reason {
        LEAVE,
        DEATH
    }

}
