/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.players;

import net.auroramc.core.api.AuroraMCAPI;
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
    private final Objective altObjective;
    private boolean mainObjectiveActive;

    public PlayerScoreboard(AuroraMCPlayer player, Scoreboard scoreboard) {
        this.player = player;
        this.scoreboard = scoreboard;
        this.lines = new HashMap<>();
        objective = scoreboard.registerNewObjective("scoreboard", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        mainObjectiveActive = true;

        altObjective = scoreboard.registerNewObjective("alt_scoreboard", "dummy");
    }

    public String getLine(int i) {
        return lines.get(i);
    }

    public void setLine(int line, String message) {
        message = AuroraMCAPI.getFormatter().convert(message);
        if (lines.containsKey(line)) {
            scoreboard.resetScores(lines.get(line));
        }
        lines.put(line, message);
        if (mainObjectiveActive) {
            altObjective.getScore(message).setScore(line);
            altObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective.setDisplaySlot(null);
            objective.getScore(message).setScore(line);
            mainObjectiveActive = false;
        } else {
            objective.getScore(message).setScore(line);
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            altObjective.setDisplaySlot(null);
            altObjective.getScore(message).setScore(line);
            mainObjectiveActive = true;
        }
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
        altObjective.setDisplayName(AuroraMCAPI.getFormatter().convert(title));
    }
}
