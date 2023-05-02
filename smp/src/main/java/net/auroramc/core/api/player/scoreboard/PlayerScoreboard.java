/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.player.scoreboard;

import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.api.utils.TextFormatter;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Map;

public class PlayerScoreboard {

    private AuroraMCPlayer player;
    private final Scoreboard scoreboard;
    private final Map<Integer, ScoreboardLine> lines;
    private final Objective objective;

    public PlayerScoreboard(AuroraMCPlayer player, Scoreboard scoreboard) {
        this.player = player;
        this.scoreboard = scoreboard;
        this.lines = new HashMap<>();
        objective = scoreboard.registerNewObjective("scoreboard", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public ScoreboardLine getLine(int i) {
        return lines.get(i);
    }

    public void setPlayer(AuroraMCPlayer player) {
        this.player = player;
    }

    public void setLine(int line, String message) {
        message = TextFormatter.convert(message);
        if (lines.containsKey(line)) {
            if (!lines.get(line).getText().equals(message)) {
                lines.get(line).setText(message);
            }
            return;
        }
        ScoreboardLine line1 = new ScoreboardLine(scoreboard, objective, line, message);
        lines.put(line, line1);
        line1.apply();
    }

    public void clearLine(int line) {
        if (lines.containsKey(line)) {
            ScoreboardLine sbLine = lines.remove(line);
            sbLine.remove();
        }
    }

    public void clear() {
        for (ScoreboardLine line : lines.values()) {
            line.remove();
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
        objective.setDisplayName(TextFormatter.convert(title));
    }
}
