/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.utils.holograms;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.holograms.personal.HologramClickHandler;
import net.auroramc.core.api.utils.holograms.personal.PersonalHologramLine;
import net.auroramc.core.api.utils.holograms.universal.UniversalHologramLine;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.PacketPlayOutUpdateEntityNBT;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hologram {

    private final Map<Integer, HologramLine> lines;
    private Location location;
    private boolean spawned;
    private final AuroraMCPlayer player;
    private final HologramClickHandler clickHandler;
    protected List<AuroraMCPlayer> trackedPlayers;

    private static int maxViewRange;

    static {
        maxViewRange = (Bukkit.getViewDistance() - 1) * 16;
        if (maxViewRange > 64) maxViewRange = 64;
    }

    public Hologram(AuroraMCPlayer player, Location location, HologramClickHandler clickHandler) {
        this.location = location;
        spawned = false;
        lines = new HashMap<>();
        this.player = player;
        this.clickHandler = clickHandler;
        trackedPlayers = new ArrayList<>();
    }

    public void move(Location location) {
        this.location = location;
        if (spawned) {
            for (HologramLine line : lines.values()) {
                line.move();
            }
        }
    }

    public void addLine(int line, String text) {
        if (line < 1) {
            throw new IllegalArgumentException("A line number below 1 was used.");
        }
        if (!lines.containsKey(line)) {
            if (line != lines.size() + 1) {
                line = lines.size() + 1;
            }
        } else {
            for (int i = lines.size();i >= line;line--) {
                HologramLine line1 = lines.get(i);
                line1.setLine(i + 1);
                lines.put(i + 1, line1);
                if (spawned) {
                    line1.move();
                }
            }
        }

        HologramLine hl;
        if (player != null) {
            hl = new PersonalHologramLine(this, text, line);
        } else {
            hl = new UniversalHologramLine(this, text, line);
        }
        lines.put(line, hl);
    }

    public void removeLine(int line) {
        if (line < 1) {
            throw new IllegalArgumentException("A line number below 1 was used.");
        }
        if (line > lines.size()) {
            throw new IndexOutOfBoundsException();
        }
        HologramLine hl = lines.remove(line);
        hl.despawn();
        for (int i = line + 1;i <= lines.size();line++) {
            HologramLine line1 = lines.remove(i);
            line1.setLine(i - 1);
            lines.put(i - 1, line1);
            if (spawned) {
                line1.move();
            }
        }
    }

    public void setLines(List<HologramLine> lines) {
        if (player != null) {
            AuroraMCAPI.deregisterHologram(this);
        }
        for (HologramLine line : this.lines.values()) {
            if (spawned) line.despawn();
        }
        this.lines.clear();
        int i = 1;
        for (HologramLine line : lines) {
            this.lines.put(i, line);
            line.setHologram(this);
            if (spawned) line.spawn();
            i++;
        }
        if (spawned) {
            AuroraMCAPI.registerHologram(this);
        }
    }

    public void spawn() {
        if (spawned) return;
        for (HologramLine line : lines.values()) {
            line.spawn();
        }
        spawned = true;
        AuroraMCAPI.registerHologram(this);
    }

    public void despawn() {
        if (!spawned) return;
        AuroraMCAPI.deregisterHologram(this);
        for (HologramLine line : lines.values()) {
            line.despawn();
        }
        spawned = false;

    }

    public void update() {
        if (!spawned) return;
        AuroraMCAPI.deregisterHologram(this);
        for (HologramLine line : lines.values()) {
            line.update();
        }
        AuroraMCAPI.registerHologram(this);
    }

    public void onJoin(AuroraMCPlayer player) {
        if (!spawned || (this.player != null && !player.equals(this.player))) return;
        if (shouldTrack(player)) {
            for (HologramLine line : lines.values()) {
                line.onJoin(player);
            }
        }
    }

    public void onClick() {
        if (player != null && clickHandler != null) {
            clickHandler.onClick(player);
        }
    }

    public AuroraMCPlayer getPlayer() {
        return player;
    }

    public void onLeave(AuroraMCPlayer player) {
        if (!spawned || (this.player != null && !player.equals(this.player))) return;
        for (HologramLine line : lines.values()) {
            line.onLeave(player);
        }

        trackedPlayers.remove(player);
    }

    public void moveCheck(AuroraMCPlayer player) {
        if (!spawned || (this.player != null && !player.equals(this.player))) return;
        if (shouldTrack(player)) {
            if (!trackedPlayers.contains(player)) {
                trackedPlayers.add(player);
                PlayerConnection con = ((CraftPlayer) player.getPlayer()).getHandle().playerConnection;
                for (HologramLine line : lines.values()) {
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            PacketPlayOutSpawnEntityLiving packet1 = new PacketPlayOutSpawnEntityLiving(line.getArmorStand());
                            PacketPlayOutUpdateEntityNBT packet2 = new PacketPlayOutUpdateEntityNBT(line.getArmorStand().getId(), line.getArmorStand().getNBTTag());
                            con.sendPacket(packet1);
                            con.sendPacket(packet2);
                        }
                    }.runTask(AuroraMCAPI.getCore());
                }
            }
        } else {
            if (trackedPlayers.contains(player)) {
                PlayerConnection con = ((CraftPlayer) player.getPlayer()).getHandle().playerConnection;
                for (HologramLine line : lines.values()) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(line.getArmorStand().getId());
                            con.sendPacket(packet);
                        }
                    }.runTask(AuroraMCAPI.getCore());
                }
                trackedPlayers.remove(player);
            }
        }
    }

    public boolean shouldTrack(AuroraMCPlayer player) {
        if (!player.getPlayer().getLocation().getWorld().equals(location.getWorld())) {
            return false;
        }
        double diffX = Math.abs(player.getPlayer().getLocation().getX() - location.getX());
        double diffZ = Math.abs(player.getPlayer().getLocation().getZ() - location.getZ());

        return diffX <= maxViewRange
                && diffZ <= maxViewRange;
    }

    public Location getLocation() {
        return location;
    }

    public Map<Integer, HologramLine> getLines() {
        return lines;
    }

    public boolean isPersonal() {
        return player != null;
    }
}
