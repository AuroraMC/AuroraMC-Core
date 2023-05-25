/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.api.utils;

import org.json.JSONObject;

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
        DEATH,
        HOME
    }

    public JSONObject toJSON() {
        JSONObject object = new JSONObject();
        if (dimension != null) {
            object.put("dimension", dimension.name());
        }
        object.put("x", x);
        object.put("y", y);
        object.put("z", z);
        object.put("pitch", pitch);
        object.put("yaw", yaw);

        if (reason != null) {
            object.put("reason", reason.name());
        }
        return object;
    }

    public static SMPLocation fromJSON(JSONObject object) {
        Dimension dimension = object.has("dimension")?Dimension.valueOf(object.getString("dimension")):null;
        double x = object.getDouble("x");
        double y = object.getDouble("y");
        double z = object.getDouble("z");
        float yaw = object.getFloat("yaw");
        float pitch = object.getFloat("pitch");
        Reason reason = object.has("reason")?Reason.valueOf(object.getString("reason")):null;
        return new SMPLocation(dimension, x, y, z, pitch, yaw, reason);
    }

}

