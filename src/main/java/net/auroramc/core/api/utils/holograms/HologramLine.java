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

    private static int maxViewRange;

    static {
        maxViewRange = (Bukkit.getViewDistance() - 1) * 16;
        if (maxViewRange > 64) maxViewRange = 64;
    }

    protected EntityArmorStand armorStand;
    protected String text;
    protected Hologram hologram;

    protected int line;
    protected List<AuroraMCPlayer> trackedPlayers;

    public HologramLine(Hologram hologram, String text, int line) {
        this.hologram = hologram;
        this.text = text;
        this.line = line;
        this.armorStand = null;
        trackedPlayers = new ArrayList<>();
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

    public void moveCheck(AuroraMCPlayer player) {
        if (shouldTrack(player)) {
            if (!trackedPlayers.contains(player)) {
                trackedPlayers.add(player);
                PlayerConnection con = ((CraftPlayer) player.getPlayer()).getHandle().playerConnection;
                PacketPlayOutSpawnEntityLiving packet1 = new PacketPlayOutSpawnEntityLiving(armorStand);
                PacketPlayOutUpdateEntityNBT packet2 = new PacketPlayOutUpdateEntityNBT(armorStand.getId(), armorStand.getNBTTag());
                con.sendPacket(packet1);
                con.sendPacket(packet2);
            }
        } else {
            if (trackedPlayers.contains(player)) {
                PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(armorStand.getId());
                PlayerConnection con = ((CraftPlayer) player.getPlayer()).getHandle().playerConnection;
                con.sendPacket(packet);
                trackedPlayers.remove(player);
            }
        }
    }

    public abstract void onJoin(AuroraMCPlayer player);

    public abstract void onLeave(AuroraMCPlayer player);

    public boolean shouldTrack(AuroraMCPlayer player) {
        if (!player.getPlayer().getLocation().getWorld().equals(hologram.getLocation().getWorld())) {
            return false;
        }
        double diffX = Math.abs(player.getPlayer().getLocation().getX() - hologram.getLocation().getX());
        double diffZ = Math.abs(player.getPlayer().getLocation().getZ() - hologram.getLocation().getZ());

        return diffX <= maxViewRange
                && diffZ <= maxViewRange;
    }

    public void setHologram(Hologram hologram) {
        this.hologram = hologram;
    }

    public EntityArmorStand getArmorStand() {
        return armorStand;
    }
}
