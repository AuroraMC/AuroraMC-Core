/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.utils.holograms.universal;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.holograms.Hologram;
import net.auroramc.core.api.utils.holograms.HologramLine;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

public class UniversalHologramLine extends HologramLine {

    private static int maxViewRange;

    static {
        maxViewRange = (Bukkit.getViewDistance() - 1) * 16;
        if (maxViewRange > 64) maxViewRange = 64;
    }

    public UniversalHologramLine(Hologram hologram, String text, int line) {
        super(hologram, text, line);
        this.armorStand = null;
    }

    public void setText(String text) {
        this.text = text;
        if (armorStand != null) {
            armorStand.setCustomName(TextFormatter.convert(text));
            PacketPlayOutUpdateEntityNBT packet = new PacketPlayOutUpdateEntityNBT(armorStand.getId(), armorStand.getNBTTag());
            for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
                if (hologram.shouldTrack(player)) {
                    PlayerConnection con = player.getCraft().getHandle().playerConnection;
                    con.sendPacket(packet);
                }
            }
        }
    }

    public void spawn() {
        if (armorStand != null) return;
        Location location = hologram.getLocation().clone().getBlock().getLocation().clone().add(0.5, 0, 0.5);
        location.setY(hologram.getLocation().getY() + ((hologram.getLines().size() - (line + 1)) * 0.25));
        armorStand = new EntityArmorStand(((CraftWorld) Bukkit.getWorld("world")).getHandle(), location.getX(), location.getY(), location.getZ());
        armorStand.setArms(false);
        //Set as marker
        armorStand.n(true);
        armorStand.setGravity(false);
        armorStand.setBasePlate(false);
        armorStand.setCustomName(TextFormatter.convert(text));
        armorStand.setCustomNameVisible(true);
        armorStand.setSmall(true);
        armorStand.setInvisible(true);
        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(armorStand);
        PacketPlayOutUpdateEntityNBT packet2 = new PacketPlayOutUpdateEntityNBT(armorStand.getId(), armorStand.getNBTTag());
        for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
            if (!hologram.shouldTrack(player)) {
                continue;
            }
            PlayerConnection con = player.getCraft().getHandle().playerConnection;
            con.sendPacket(packet);
            con.sendPacket(packet2);
        }
    }

    public void move() {
        if (armorStand == null) return;
        Location location = hologram.getLocation().clone().getBlock().getLocation().clone().add(0.5, 0, 0.5);
        location.setY(hologram.getLocation().getY() + ((hologram.getLines().size() - (line + 1)) * 0.25));
        armorStand.locX = location.getX();
        armorStand.locY = location.getY();
        armorStand.locZ = location.getZ();
        PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(armorStand);
        for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
            if (hologram.shouldTrack(player)) {
                PlayerConnection con = player.getCraft().getHandle().playerConnection;
                con.sendPacket(packet);
            }
        }
    }

    public void despawn() {
        if (armorStand == null) return;
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(armorStand.getId());
        for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
            if (!hologram.shouldTrack(player)) {
                continue;
            }
            PlayerConnection con = player.getCraft().getHandle().playerConnection;
            con.sendPacket(packet);
        }
        armorStand.dead = true;
        armorStand = null;
    }


    public void onJoin(AuroraMCServerPlayer player) {
        PlayerConnection con = player.getCraft().getHandle().playerConnection;
        PacketPlayOutSpawnEntityLiving packet1 = new PacketPlayOutSpawnEntityLiving(armorStand);
        PacketPlayOutUpdateEntityNBT packet2 = new PacketPlayOutUpdateEntityNBT(armorStand.getId(), armorStand.getNBTTag());
        con.sendPacket(packet1);
        con.sendPacket(packet2);

    }

    public void onLeave(AuroraMCServerPlayer player) {}

}
