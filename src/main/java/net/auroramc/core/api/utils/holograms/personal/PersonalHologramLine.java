/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.utils.holograms.personal;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.holograms.Hologram;
import net.auroramc.core.api.utils.holograms.HologramLine;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

public class PersonalHologramLine extends HologramLine {
    
    public PersonalHologramLine(Hologram hologram, String text, int line) {
        super(hologram, text, line);
    }

    public void setText(String text) {
        this.text = text;
        if (armorStand != null) {
            armorStand.setCustomName(AuroraMCAPI.getFormatter().convert(text));
            PacketPlayOutUpdateEntityNBT packet = new PacketPlayOutUpdateEntityNBT(armorStand.getId(), armorStand.getNBTTag());
            if (!shouldTrack(hologram.getPlayer())) {
                if (trackedPlayers.contains(hologram.getPlayer())) {
                    trackedPlayers.remove(hologram.getPlayer());
                    PacketPlayOutEntityDestroy packet1 = new PacketPlayOutEntityDestroy(armorStand.getId());
                    PlayerConnection con = ((CraftPlayer) hologram.getPlayer().getPlayer()).getHandle().playerConnection;
                    con.sendPacket(packet1);
                }
                return;
            }
            if (!trackedPlayers.contains(hologram.getPlayer())) {
                trackedPlayers.add(hologram.getPlayer());
                PlayerConnection con = ((CraftPlayer) hologram.getPlayer().getPlayer()).getHandle().playerConnection;
                PacketPlayOutSpawnEntityLiving packet1 = new PacketPlayOutSpawnEntityLiving(armorStand);
                PacketPlayOutUpdateEntityNBT packet2 = new PacketPlayOutUpdateEntityNBT(armorStand.getId(), armorStand.getNBTTag());
                con.sendPacket(packet1);
                con.sendPacket(packet2);
            }
            PlayerConnection con = ((CraftPlayer) hologram.getPlayer().getPlayer()).getHandle().playerConnection;
            con.sendPacket(packet);
        }
    }

    public void spawn() {
        if (armorStand != null) return;
        Location location = hologram.getLocation().getBlock().getLocation().add(0.5, ((hologram.getLines().size() - (line + 1)) * 0.3), 0.5);
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
        if (!shouldTrack(hologram.getPlayer())) {
            return;
        }
        if (!trackedPlayers.contains(hologram.getPlayer())) {
            trackedPlayers.add(hologram.getPlayer());
        }
        PlayerConnection con = ((CraftPlayer) hologram.getPlayer().getPlayer()).getHandle().playerConnection;
        con.sendPacket(packet);
        con.sendPacket(packet2);
    }

    public void move() {
        if (armorStand == null) return;
        Location location = hologram.getLocation().getBlock().getLocation().add(0.5, ((hologram.getLines().size() - (line + 1)) * 0.3), 0.5);
        armorStand.locX = location.getX();
        armorStand.locY = location.getY();
        armorStand.locZ = location.getZ();
        PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(armorStand);
        if (!shouldTrack(hologram.getPlayer())) {
            if (trackedPlayers.contains(hologram.getPlayer())) {
                trackedPlayers.remove(hologram.getPlayer());
                PacketPlayOutEntityDestroy packet1 = new PacketPlayOutEntityDestroy(armorStand.getId());
                PlayerConnection con = ((CraftPlayer) hologram.getPlayer().getPlayer()).getHandle().playerConnection;
                con.sendPacket(packet1);
            }
            return;
        }
        if (!trackedPlayers.contains(hologram.getPlayer())) {
            trackedPlayers.add(hologram.getPlayer());
            PlayerConnection con = ((CraftPlayer) hologram.getPlayer().getPlayer()).getHandle().playerConnection;
            PacketPlayOutSpawnEntityLiving packet1 = new PacketPlayOutSpawnEntityLiving(armorStand);
            PacketPlayOutUpdateEntityNBT packet2 = new PacketPlayOutUpdateEntityNBT(armorStand.getId(), armorStand.getNBTTag());
            con.sendPacket(packet1);
            con.sendPacket(packet2);
        } else {
            PlayerConnection con = ((CraftPlayer) hologram.getPlayer().getPlayer()).getHandle().playerConnection;
            con.sendPacket(packet);
        }
    }

    public void despawn() {
        if (armorStand == null) return;
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(armorStand.getId());
        if (!trackedPlayers.contains(hologram.getPlayer())) {
            
            return;
        }
        PlayerConnection con = ((CraftPlayer) hologram.getPlayer().getPlayer()).getHandle().playerConnection;
        con.sendPacket(packet);
        armorStand.dead = true;
        armorStand = null;
        trackedPlayers.clear();
    }

    public void setLine(int line) {
        this.line = line;
    }

    public void onJoin(AuroraMCPlayer player) {
    }

    public void onLeave(AuroraMCPlayer player) {
        if (armorStand != null) {
            armorStand.dead = true;
            armorStand = null;
            trackedPlayers.clear();
        }
    }

    

    public void setHologram(Hologram hologram) {
        this.hologram = hologram;
    }

    public EntityArmorStand getArmorStand() {
        return armorStand;
    }
}
