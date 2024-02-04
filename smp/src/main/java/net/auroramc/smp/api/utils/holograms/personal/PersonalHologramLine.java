/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.api.utils.holograms.personal;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import net.auroramc.smp.api.utils.holograms.Hologram;
import net.auroramc.smp.api.utils.holograms.HologramLine;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftArmorStand;

public class PersonalHologramLine extends HologramLine {
    
    public PersonalHologramLine(Hologram hologram, String text, int line) {
        super(hologram, text, line);
    }

    public void setText(String text) {
        this.text = text;
        if (armorStand != null && !armorStand.isDead()) {
            armorStand.setCustomName(TextFormatter.convert(text));
            PlayerConnection con = hologram.getPlayer().getCraft().getHandle().c;
            con.a(new PacketPlayOutEntityMetadata(armorStand.getEntityId(), armorStand.getHandle().aj().b()));
        }
    }

    public void spawn() {
        if (armorStand != null && !armorStand.isDead()) return;
        Location location = hologram.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
        location.setY(hologram.getLocation().getY() + ((hologram.getLines().size() - (line + 1)) * 0.25));
        if (armorStand != null) {
            armorStand.getHandle().dD();
            armorStand.teleport(location);
        } else {
            if (Hologram.getUsedEntities().size() > 0) {
                armorStand = (CraftArmorStand) Hologram.getUsedEntities().pop().getBukkitEntity();
                armorStand.getHandle().dD();
                armorStand.teleport(location);
            } else {
                armorStand = (CraftArmorStand) new EntityArmorStand(((CraftWorld) Bukkit.getWorld("world")).getHandle(), location.getX(), location.getY(), location.getZ()).getBukkitEntity();
            }
        }
        armorStand.setArms(false);
        //Set as marker
        armorStand.setMarker(true);
        armorStand.setGravity(false);
        armorStand.setBasePlate(false);
        armorStand.setCustomName(TextFormatter.convert(text));
        armorStand.setCustomNameVisible(true);
        armorStand.setSmall(true);
        armorStand.setInvisible(true);
        PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity(armorStand.getHandle());
        PacketPlayOutEntityMetadata packet2 = new PacketPlayOutEntityMetadata(armorStand.getEntityId(), armorStand.getHandle().aj().b());
        if (!hologram.shouldTrack(hologram.getPlayer())) {
            return;
        }
        PlayerConnection con = hologram.getPlayer().getCraft().getHandle().c;
        con.a(packet);
        con.a(packet2);
    }

    public void move() {
        if (armorStand == null || armorStand.isDead()) return;
        Location location = hologram.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
        location.setY(hologram.getLocation().getY() + ((hologram.getLines().size() - (line + 1)) * 0.25));
        armorStand.teleport(location);
        PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(armorStand.getHandle());
        PlayerConnection con = hologram.getPlayer().getCraft().getHandle().c;
        con.a(packet);
    }

    public void despawn() {
        if (armorStand == null || armorStand.isDead()) return;
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(armorStand.getEntityId());
        PlayerConnection con = hologram.getPlayer().getCraft().getHandle().c;
        con.a(packet);
        armorStand.getHandle().b(Entity.RemovalReason.a);
    }

    public void setLine(int line) {
        this.line = line;
    }

    public void onJoin(AuroraMCServerPlayer player) {
    }

    public void onLeave(AuroraMCServerPlayer player) {
        if (armorStand != null) {
            armorStand.getHandle().b(Entity.RemovalReason.a);
            armorStand = null;
        }
    }

    

    public void setHologram(Hologram hologram) {
        this.hologram = hologram;
    }

    public CraftArmorStand getArmorStand() {
        return armorStand;
    }
}
