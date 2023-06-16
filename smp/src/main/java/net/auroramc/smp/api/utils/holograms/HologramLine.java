/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.api.utils.holograms;

import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftArmorStand;

public abstract class HologramLine {

    protected CraftArmorStand armorStand;
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

    public CraftArmorStand getArmorStand() {
        return armorStand;
    }
}
