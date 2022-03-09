/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.players.scoreboard;

import net.auroramc.core.api.AuroraMCAPI;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ScoreboardLine {


    private final Objective objective;
    private final int line;
    private String text;
    private final Team team;
    private final String user;

    public ScoreboardLine(Scoreboard scoreboard, Objective objective, int line, String text) {
        this.line = line;
        this.team = scoreboard.registerNewTeam("line" + line);
        this.user = new String(new char[line]).replace("\0", "֍");
        team.addEntry(user);
        this.text = text;
        if (this.text.length() > 16) {
            team.setPrefix(this.text.substring(0, 16));
            String suffix = this.text.substring(16);
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
        objective.getScore(user).setScore(line);
    }

    public void remove() {
        objective.getScoreboard().resetScores(user);
    }

    public void setText(String text) {
        this.text = text;
        if (this.text.length() > 16) {
            team.setPrefix(this.text.substring(0, 16));
            String suffix = this.text.substring(16);
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
