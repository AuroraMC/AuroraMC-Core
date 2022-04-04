/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.players.scoreboard;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ScoreboardLine {

    private static final char[] colours = {'0','1','2','3','4','5','6','7','8','9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private final Objective objective;
    private final int line;
    private String text;
    private final Team team;

    public ScoreboardLine(Scoreboard scoreboard, Objective objective, int line, String text) {
        this.line = line;
        this.team = scoreboard.registerNewTeam("line" + line);
        team.addEntry("§" + colours[line]);
        this.text = text;
        if (this.text.length() > 16) {
            team.setPrefix(this.text.substring(0, 16));
            String suffix = ChatColor.getLastColors(team.getPrefix()) + this.text.substring(16);
            if (suffix.length() > 16) {
                suffix = suffix.substring(0, 16);
            }
            team.setSuffix(suffix);
        } else {
            team.setPrefix(this.text);
        }
        this.objective = objective;
    }

    public void apply() {
        objective.getScore("§" + colours[line]).setScore(line);
    }

    public void remove() {
        objective.getScoreboard().resetScores("§" + colours[line]);
        team.unregister();
    }

    public void setText(String text) {
        this.text = text;
        if (this.text.length() > 16) {
            team.setPrefix(this.text.substring(0, 16));
            String lastColor = ChatColor.getLastColors(team.getPrefix());
            if (lastColor.equals("")) {
                lastColor = "§r";
            }
            String suffix = lastColor + this.text.substring(16);
            if (suffix.length() > 16) {
                suffix = suffix.substring(0, 16);
            }
            team.setSuffix(suffix);
        } else {
            team.setPrefix(text);
            team.setSuffix("");
        }
    }

    public int getLine() {
        return line;
    }

    public String getText() {
        return text;
    }
}