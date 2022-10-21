/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.utils.holograms;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

import java.util.ArrayList;
import java.util.List;

public class HologramLine {

    private static int maxViewRange;

    static {
        maxViewRange = (Bukkit.getViewDistance() - 1) * 16;
        if (maxViewRange > 64) maxViewRange = 64;
    }

    private EntityArmorStand armorStand;
    private String text;
    private Hologram hologram;
    private HologramClickHandler clickHandler;
    private int line;
    private List<AuroraMCPlayer> trackedPlayers;

    public HologramLine(Hologram hologram, String text, int line, HologramClickHandler clickHandler) {
        this.hologram = hologram;
        this.text = text;
        this.line = line;
        this.clickHandler = clickHandler;
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

    public void setText(String text) {
        this.text = text;
        if (armorStand != null) {
            armorStand.setCustomName(AuroraMCAPI.getFormatter().convert(text));
            PacketPlayOutUpdateEntityNBT packet = new PacketPlayOutUpdateEntityNBT(armorStand.getId(), armorStand.getNBTTag());
            for (AuroraMCPlayer player : AuroraMCAPI.getPlayers()) {
                if (!shouldTrack(player)) {
                    if (trackedPlayers.contains(player)) {
                        trackedPlayers.remove(player);
                        PacketPlayOutEntityDestroy packet1 = new PacketPlayOutEntityDestroy(armorStand.getId());
                        PlayerConnection con = ((CraftPlayer) player.getPlayer()).getHandle().playerConnection;
                        con.sendPacket(packet1);
                    }
                    continue;
                }
                if (!trackedPlayers.contains(player)) {
                    trackedPlayers.add(player);
                    PlayerConnection con = ((CraftPlayer) player.getPlayer()).getHandle().playerConnection;
                    PacketPlayOutSpawnEntityLiving packet1 = new PacketPlayOutSpawnEntityLiving(armorStand);
                    PacketPlayOutUpdateEntityNBT packet2 = new PacketPlayOutUpdateEntityNBT(armorStand.getId(), armorStand.getNBTTag());
                    con.sendPacket(packet1);
                    con.sendPacket(packet2);
                }
                PlayerConnection con = ((CraftPlayer) player.getPlayer()).getHandle().playerConnection;
                con.sendPacket(packet);
            }
        }
    }

    public void spawn() {
        if (armorStand != null) return;
        Location location = hologram.getLocation().getBlock().getLocation().add(0.5, ((hologram.getLines().size()- (line + 1)) * 0.3), 0.5);
        armorStand = new EntityArmorStand(((CraftWorld) Bukkit.getWorld("world")).getHandle(), location.getX(), location.getY(), location.getZ());
        armorStand.setArms(false);
        //Set as marker
        armorStand.n(true);
        armorStand.setGravity(false);
        armorStand.setBasePlate(false);
        armorStand.setCustomName(AuroraMCAPI.getFormatter().convert(text));
        armorStand.setCustomNameVisible(true);
        armorStand.setSmall(true);
        armorStand.setInvisible(true);
        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(armorStand);
        PacketPlayOutUpdateEntityNBT packet2 = new PacketPlayOutUpdateEntityNBT(armorStand.getId(), armorStand.getNBTTag());
        for (AuroraMCPlayer player : AuroraMCAPI.getPlayers()) {
            if (!shouldTrack(player)) {
                continue;
            }
            if (!trackedPlayers.contains(player)) {
                trackedPlayers.add(player);
            }
            PlayerConnection con = ((CraftPlayer) player.getPlayer()).getHandle().playerConnection;
            con.sendPacket(packet);
            con.sendPacket(packet2);
        }
    }

    public void move() {
        if (armorStand == null) return;
        Location location = hologram.getLocation().getBlock().getLocation().add(0.5, ((hologram.getLines().size()- (line + 1)) * 0.3), 0.5);
        armorStand.locX = location.getX();
        armorStand.locY = location.getY();
        armorStand.locZ = location.getZ();
        PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(armorStand);
        for (AuroraMCPlayer player : AuroraMCAPI.getPlayers()) {
            if (!shouldTrack(player)) {
                if (trackedPlayers.contains(player)) {
                    trackedPlayers.remove(player);
                    PacketPlayOutEntityDestroy packet1 = new PacketPlayOutEntityDestroy(armorStand.getId());
                    PlayerConnection con = ((CraftPlayer) player.getPlayer()).getHandle().playerConnection;
                    con.sendPacket(packet1);
                }
                continue;
            }
            if (!trackedPlayers.contains(player)) {
                trackedPlayers.add(player);
                PlayerConnection con = ((CraftPlayer) player.getPlayer()).getHandle().playerConnection;
                PacketPlayOutSpawnEntityLiving packet1 = new PacketPlayOutSpawnEntityLiving(armorStand);
                PacketPlayOutUpdateEntityNBT packet2 = new PacketPlayOutUpdateEntityNBT(armorStand.getId(), armorStand.getNBTTag());
                con.sendPacket(packet1);
                con.sendPacket(packet2);
            } else {
                PlayerConnection con = ((CraftPlayer) player.getPlayer()).getHandle().playerConnection;
                con.sendPacket(packet);
            }
        }
    }

    public void despawn() {
        if (armorStand == null) return;
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(armorStand.getId());
        for (AuroraMCPlayer player : AuroraMCAPI.getPlayers()) {
            if (!trackedPlayers.contains(player)) {
                continue;
            }
            PlayerConnection con = ((CraftPlayer) player.getPlayer()).getHandle().playerConnection;
            con.sendPacket(packet);
        }
        armorStand.dead = true;
        armorStand = null;
        trackedPlayers.clear();
    }

    public void onClick() {
        if (clickHandler != null) {
            clickHandler.onClick(this);
        }
    }

    public void setLine(int line) {
        this.line = line;
    }

    public void onJoin(AuroraMCPlayer player) {
        if (shouldTrack(player)) {
            trackedPlayers.add(player);
            PlayerConnection con = ((CraftPlayer) player.getPlayer()).getHandle().playerConnection;
            PacketPlayOutSpawnEntityLiving packet1 = new PacketPlayOutSpawnEntityLiving(armorStand);
            PacketPlayOutUpdateEntityNBT packet2 = new PacketPlayOutUpdateEntityNBT(armorStand.getId(), armorStand.getNBTTag());
            con.sendPacket(packet1);
            con.sendPacket(packet2);
        }
    }

    public void onLeave(AuroraMCPlayer player) {
        trackedPlayers.remove(player);
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
}
