/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.utils.holograms;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.holograms.personal.HologramClickHandler;
import net.auroramc.core.api.utils.holograms.personal.PersonalHologramLine;
import net.auroramc.core.api.utils.holograms.universal.UniversalHologramLine;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hologram {

    private final Map<Integer, HologramLine> lines;
    private Location location;
    private boolean spawned;
    private final AuroraMCPlayer player;
    private final HologramClickHandler clickHandler;

    public Hologram(AuroraMCPlayer player, Location location, HologramClickHandler clickHandler) {
        this.location = location;
        spawned = false;
        lines = new HashMap<>();
        this.player = player;
        this.clickHandler = clickHandler;
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
                line1.move();
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
            line1.move();
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
        }
        if (spawned && player != null) {
            AuroraMCAPI.registerHologram(this);
        }
    }

    public void spawn() {
        if (spawned) return;
        for (HologramLine line : lines.values()) {
            line.spawn();
        }
        spawned = true;
        if (player != null) {
            AuroraMCAPI.registerHologram(this);
        }
    }

    public void despawn() {
        if (!spawned) return;
        for (HologramLine line : lines.values()) {
            line.despawn();
        }
        spawned = false;
        if (player != null) {
            AuroraMCAPI.deregisterHologram(this);
        }
    }

    public void update() {
        if (!spawned) return;
        if (player != null) {
            AuroraMCAPI.deregisterHologram(this);
        }
        for (HologramLine line : lines.values()) {
            line.update();
        }
        if (player != null) {
            AuroraMCAPI.registerHologram(this);
        }
    }

    public void onJoin(AuroraMCPlayer player) {
        if (!spawned || (this.player != null && !player.equals(this.player))) return;
        for (HologramLine line : lines.values()) {
            line.onJoin(player);
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
    }

    public void moveCheck(AuroraMCPlayer player) {
        if (!spawned || (this.player != null && !player.equals(this.player))) return;
        for (HologramLine line : lines.values()) {
            line.moveCheck(player);
        }
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