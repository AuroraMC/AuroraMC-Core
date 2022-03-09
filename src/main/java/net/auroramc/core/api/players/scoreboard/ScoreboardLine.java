/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.players.scoreboard;

import net.auroramc.core.api.AuroraMCAPI;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ScoreboardLine {

    private static char[] colours = {'0','1','2','3','4','5','6','7','8','9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private final Objective objective;
    private final int line;
    private String text;
    private final Team team;

    public ScoreboardLine(Scoreboard scoreboard, Objective objective, int line, String text) {
        this.line = line;
        this.team = scoreboard.registerNewTeam("line" + line);
        team.addEntry("§" + colours[line]);
        team.setPrefix(AuroraMCAPI.getFormatter().convert(text));
        this.text = text;
        this.objective = objective;
    }

    public void apply() {
        objective.getScore("§" + colours[line]).setScore(line);
    }

    public void remove() {
        objective.getScoreboard().resetScores("§" + colours[line]);
    }

    public void setText(String text) {
        this.text = text;
        team.setPrefix(AuroraMCAPI.getFormatter().convert(text));
    }

    public int getLine() {
        return line;
    }

    public String getText() {
        return text;
    }
}
