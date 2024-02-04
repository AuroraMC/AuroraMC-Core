/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.api.utils.holograms;

import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import net.auroramc.smp.api.utils.holograms.personal.HologramClickHandler;
import net.auroramc.smp.api.utils.holograms.personal.PersonalHologramLine;
import net.auroramc.smp.api.utils.holograms.universal.UniversalHologramLine;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Hologram {

    private static Deque<EntityArmorStand> usedEntities = new ArrayDeque<>();

    private final Map<Integer, HologramLine> lines;
    private Location location;
    private boolean spawned;
    private final AuroraMCServerPlayer player;
    private final HologramClickHandler clickHandler;
    protected List<AuroraMCServerPlayer> trackedPlayers;

    private static int maxViewRange;

    static {
        maxViewRange = (Bukkit.getViewDistance() - 1) * 16;
        if (maxViewRange > 64) maxViewRange = 64;
    }

    public Hologram(AuroraMCServerPlayer player, Location location, HologramClickHandler clickHandler) {
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
        boolean move = false;
        if (!lines.containsKey(line)) {
            if (line != lines.size() + 1) {
                line = lines.size() + 1;
            }
            move = true;
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
        if (spawned) {
            hl.spawn();
        }
        if (spawned && move) {
            for (HologramLine hl2 : lines.values()) {
                hl2.move();
            }
        }
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
        usedEntities.add(hl.getArmorStand().getHandle());
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
            ServerAPI.deregisterHologram(this);
        }
        for (HologramLine line : this.lines.values()) {
            if (spawned || line.armorStand != null) {
                line.despawn();
                usedEntities.addLast(line.getArmorStand().getHandle());
            }
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
            ServerAPI.registerHologram(this);
        }
    }

    public void spawn() {
        if (spawned) return;
        for (HologramLine line : lines.values()) {
            line.spawn();
        }
        spawned = true;
        ServerAPI.registerHologram(this);
    }

    public void despawn() {
        if (!spawned) return;
        ServerAPI.deregisterHologram(this);
        for (HologramLine line : lines.values()) {
            line.despawn();
        }
        spawned = false;
    }

    public void update() {
        if (!spawned) return;
        ServerAPI.deregisterHologram(this);
        for (HologramLine line : lines.values()) {
            line.update();
        }
        ServerAPI.registerHologram(this);
    }

    public void onJoin(AuroraMCServerPlayer player) {
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

    public AuroraMCServerPlayer getPlayer() {
        return player;
    }

    public void onLeave(AuroraMCServerPlayer player) {
        if (!spawned || (this.player != null && !player.equals(this.player))) return;
        for (HologramLine line : lines.values()) {
            line.onLeave(player);
        }

        trackedPlayers.remove(player);
    }

    public void moveCheck(AuroraMCServerPlayer player) {
        if (!spawned || (this.player != null && !player.equals(this.player))) return;
        if (shouldTrack(player)) {
            if (!trackedPlayers.contains(player)) {
                trackedPlayers.add(player);
                PlayerConnection con = player.getCraft().getHandle().c;
                for (HologramLine line : lines.values()) {
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            PacketPlayOutSpawnEntity packet1 = new PacketPlayOutSpawnEntity(line.getArmorStand().getHandle());
                            con.a(packet1);
                            con.a(new PacketPlayOutEntityMetadata(line.getArmorStand().getEntityId(), line.getArmorStand().getHandle().aj().b()));
                        }
                    }.runTask(ServerAPI.getCore());
                }
            }
        } else {
            if (trackedPlayers.contains(player)) {
                PlayerConnection con = player.getCraft().getHandle().c;
                for (HologramLine line : lines.values()) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(line.getArmorStand().getEntityId());
                            con.a(packet);
                        }
                    }.runTask(ServerAPI.getCore());
                }
                trackedPlayers.remove(player);
            }
        }
    }

    public boolean shouldTrack(AuroraMCServerPlayer player) {
        if (!player.getLocation().getWorld().equals(location.getWorld())) {
            return false;
        }
        double diffX = Math.abs(player.getLocation().getX() - location.getX());
        double diffZ = Math.abs(player.getLocation().getZ() - location.getZ());

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

    public static Deque<EntityArmorStand> getUsedEntities() {
        return usedEntities;
    }
}
