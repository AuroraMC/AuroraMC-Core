/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.core.api.utils.holograms;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.minecraft.server.v1_8_R3.EntityArmorStand;

public abstract class HologramLine {

    protected EntityArmorStand armorStand;
    protected String text;
    protected Hologram hologram;

    protected int line;

    public HologramLine(Hologram hologram, String text, int line) {
        this.hologram = hologram;
        this.text = text;
        this.line = line;
        this.armorStand = null;
    }

    public void update() {
        if (armorStand == null) {
            spawn();
        } else {
            move();
        }
    }

    public abstract void setText(String text);

    public abstract void spawn();

    public abstract void move();

    public abstract void despawn();

    public void setLine(int line) {
        this.line = line;
    }

    public abstract void onJoin(AuroraMCServerPlayer player);

    public abstract void onLeave(AuroraMCServerPlayer player);

    public void setHologram(Hologram hologram) {
        this.hologram = hologram;
    }

    public EntityArmorStand getArmorStand() {
        return armorStand;
    }
}
