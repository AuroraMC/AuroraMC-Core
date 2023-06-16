/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.api.utils.holograms.universal;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import net.auroramc.smp.api.utils.holograms.Hologram;
import net.auroramc.smp.api.utils.holograms.HologramLine;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftArmorStand;

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
        if (armorStand != null && !armorStand.isDead()) {
            armorStand.setCustomName(TextFormatter.convert(text));
            PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(armorStand.getEntityId(), armorStand.getHandle().aj().b());
            for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
                if (hologram.shouldTrack(player)) {
                    PlayerConnection con = player.getCraft().getHandle().b;
                    con.a(packet);
                }
            }
        }
    }

    public void spawn() {
        if (armorStand != null && !armorStand.isDead()) return;
        Location location = hologram.getLocation().clone().getBlock().getLocation().clone().add(0.5, 0, 0.5);
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
        for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
            if (!hologram.shouldTrack(player)) {
                continue;
            }
            PlayerConnection con = player.getCraft().getHandle().b;
            con.a(packet);
            con.a(packet2);
        }
    }

    public void move() {
        if (armorStand == null) return;
        Location location = hologram.getLocation().clone().getBlock().getLocation().clone().add(0.5, 0, 0.5);
        location.setY(hologram.getLocation().getY() + ((hologram.getLines().size() - (line + 1)) * 0.25));
        armorStand.teleport(location);
        PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(armorStand.getHandle());
        for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
            if (hologram.shouldTrack(player)) {
                PlayerConnection con = player.getCraft().getHandle().b;
                con.a(packet);
            }
        }
    }

    public void despawn() {
        if (armorStand == null) return;
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(armorStand.getEntityId());
        for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
            if (!hologram.shouldTrack(player)) {
                continue;
            }
            PlayerConnection con = player.getCraft().getHandle().b;
            con.a(packet);
        }
        armorStand.getHandle().b(Entity.RemovalReason.a);
    }


    public void onJoin(AuroraMCServerPlayer player) {
        PlayerConnection con = player.getCraft().getHandle().b;
        PacketPlayOutSpawnEntity packet1 = new PacketPlayOutSpawnEntity(armorStand.getHandle());
        PacketPlayOutEntityMetadata packet2 = new PacketPlayOutEntityMetadata(armorStand.getEntityId(), armorStand.getHandle().aj().b());
        con.a(packet1);
        con.a(packet2);

    }

    public void onLeave(AuroraMCServerPlayer player) {}

}
