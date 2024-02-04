/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.api.player.scoreboard;

import net.auroramc.api.AuroraMCAPI;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.logging.Level;

public class ScoreboardLine {

    private static final char[] colours = {'0','1','2','3','4','5','6','7','8','9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private final Objective objective;
    private final int line;
    private String text;
    private final Team team;

    public ScoreboardLine(Scoreboard scoreboard, Objective objective, int line, String text) {
        Team team1;
        this.line = line;
        if (scoreboard.getTeam("line" + line) != null) {
            scoreboard.getTeam("line" + line).unregister();
        }
        team1 = scoreboard.registerNewTeam("line" + line);
        try {
            team1.addEntry("§" + colours[line]);
            this.text = text;
            if (this.text.length() > 16) {
                team1.setPrefix(this.text.substring(0, 16));
                String lastColor = ChatColor.getLastColors(team1.getPrefix());
                if (lastColor.equals("")) {
                    lastColor = "§r";
                }
                String suffix = lastColor + this.text.substring(16);
                if (suffix.length() > 16) {
                    suffix = suffix.substring(0, 16);
                }
                team1.setSuffix(suffix);
            } else {
                team1.setPrefix(this.text);
            }
        } catch (IllegalStateException e) {
            AuroraMCAPI.getLogger().log(Level.WARNING, "An exception has occurred. Stack trace: ", e);
            team1 = scoreboard.registerNewTeam("line" + line);
        }
        this.team = team1;
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