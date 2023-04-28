/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.api.utils;

import net.auroramc.api.player.AuroraMCPlayer;

public class PlayerKitLevel {

    private final AuroraMCPlayer player;
    private final int id;
    private final int gameId;
    private final int kitId;
    private int level;
    private long xpIntoLevel;
    private long totalXpEarned;
    private short latestUpgrade;

    public PlayerKitLevel(AuroraMCPlayer player, int gameId, int kitId, int level, long xpIntoLevel, long totalXpEarned, short latestUpgrade) {
        this.player = player;
        this.id = player.getId();
        this.gameId = gameId;
        this.kitId = kitId;
        this.level = level;
        this.xpIntoLevel = xpIntoLevel;
        this.totalXpEarned = totalXpEarned;
        this.latestUpgrade = latestUpgrade;
    }

    public PlayerKitLevel(int player, int gameId, int kitId, int level, long xpIntoLevel, long totalXpEarned, short latestUpgrade) {
        this.player = null;
        this.id = player;
        this.gameId = gameId;
        this.kitId = kitId;
        this.level = level;
        this.xpIntoLevel = xpIntoLevel;
        this.totalXpEarned = totalXpEarned;
        this.latestUpgrade = latestUpgrade;
    }

    public int getGameId() {
        return gameId;
    }

    public int getKitId() {
        return kitId;
    }

    public int getLevel() {
        return level;
    }

    public long getTotalXpEarned() {
        return totalXpEarned;
    }

    public long getXpIntoLevel() {
        return xpIntoLevel;
    }

    public short getLatestUpgrade() {
        return latestUpgrade;
    }

}
