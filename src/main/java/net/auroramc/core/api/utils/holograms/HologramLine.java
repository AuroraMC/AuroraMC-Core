/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.utils.holograms;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

import java.util.ArrayList;
import java.util.List;

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

    public abstract void onJoin(AuroraMCPlayer player);

    public abstract void onLeave(AuroraMCPlayer player);

    public void setHologram(Hologram hologram) {
        this.hologram = hologram;
    }

    public EntityArmorStand getArmorStand() {
        return armorStand;
    }
}
