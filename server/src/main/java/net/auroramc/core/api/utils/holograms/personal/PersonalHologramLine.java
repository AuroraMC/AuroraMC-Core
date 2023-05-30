/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.core.api.utils.holograms.personal;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.holograms.Hologram;
import net.auroramc.core.api.utils.holograms.HologramLine;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

public class PersonalHologramLine extends HologramLine {
    
    public PersonalHologramLine(Hologram hologram, String text, int line) {
        super(hologram, text, line);
    }

    public void setText(String text) {
        this.text = text;
        if (armorStand != null && !armorStand.dead) {
            armorStand.setCustomName(TextFormatter.convert(text));
            PacketPlayOutUpdateEntityNBT packet = new PacketPlayOutUpdateEntityNBT(armorStand.getId(), armorStand.getNBTTag());
            PlayerConnection con = hologram.getPlayer().getCraft().getHandle().playerConnection;
            con.sendPacket(packet);
        }
    }

    public void spawn() {
        if (armorStand != null && !armorStand.dead) return;
        Location location = hologram.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
        location.setY(hologram.getLocation().getY() + ((hologram.getLines().size() - (line + 1)) * 0.25));
        if (armorStand != null) {
            armorStand.dead = false;
            armorStand.world = ((CraftWorld) Bukkit.getWorld("world")).getHandle();
            armorStand.setPosition(location.getX(), location.getY(), location.getZ());
        } else {
            if (Hologram.getUsedEntities().size() > 0) {
                armorStand = Hologram.getUsedEntities().pop();
                armorStand.dead = false;
                armorStand.world = ((CraftWorld) Bukkit.getWorld("world")).getHandle();
                armorStand.setPosition(location.getX(), location.getY(), location.getZ());
            } else {
                armorStand = new EntityArmorStand(((CraftWorld) Bukkit.getWorld("world")).getHandle(), location.getX(), location.getY(), location.getZ());
            }
        }
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
        if (!hologram.shouldTrack(hologram.getPlayer())) {
            return;
        }
        PlayerConnection con = hologram.getPlayer().getCraft().getHandle().playerConnection;
        con.sendPacket(packet);
        con.sendPacket(packet2);
    }

    public void move() {
        if (armorStand == null || armorStand.dead) return;
        Location location = hologram.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
        location.setY(hologram.getLocation().getY() + ((hologram.getLines().size() - (line + 1)) * 0.25));
        armorStand.locX = location.getX();
        armorStand.locY = location.getY();
        armorStand.locZ = location.getZ();
        PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(armorStand);
        PlayerConnection con = hologram.getPlayer().getCraft().getHandle().playerConnection;
        con.sendPacket(packet);
    }

    public void despawn() {
        if (armorStand == null || armorStand.dead) return;
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(armorStand.getId());
        PlayerConnection con = hologram.getPlayer().getCraft().getHandle().playerConnection;
        con.sendPacket(packet);
        armorStand.dead = true;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public void onJoin(AuroraMCServerPlayer player) {
    }

    public void onLeave(AuroraMCServerPlayer player) {
        if (armorStand != null) {
            armorStand.dead = true;
            armorStand = null;
        }
    }

    

    public void setHologram(Hologram hologram) {
        this.hologram = hologram;
    }

    public EntityArmorStand getArmorStand() {
        return armorStand;
    }
}
