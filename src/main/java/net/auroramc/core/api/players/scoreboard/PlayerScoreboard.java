/*
 * Copyright (c) 2021-2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.players.scoreboard;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Map;

public class PlayerScoreboard {

    private final AuroraMCPlayer player;
    private final Scoreboard scoreboard;
    private final Map<Integer, String> lines;
    private final Objective objective;

    public PlayerScoreboard(AuroraMCPlayer player, Scoreboard scoreboard) {
        this.player = player;
        this.scoreboard = scoreboard;
        this.lines = new HashMap<>();
        objective = scoreboard.registerNewObjective("scoreboard", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public String getLine(int i) {
        return lines.get(i);
    }

    public void setLine(int line, String message) {
        message = AuroraMCAPI.getFormatter().convert(message);
        if (lines.containsKey(line)) {
            if (lines.get(line).equals(message)) {
                return;
            }
            scoreboard.resetScores(lines.get(line));
        }
        lines.put(line, message);
        objective.getScore(message).setScore(line);
    }

    public void clear() {
        for (String line : lines.values()) {
            scoreboard.resetScores(line);
        }
        lines.clear();
    }

    public AuroraMCPlayer getPlayer() {
        return player;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public void setTitle(String title) {
        objective.setDisplayName(AuroraMCAPI.getFormatter().convert(title));
    }
}
