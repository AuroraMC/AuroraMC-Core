/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.stats;

public abstract class Achievement implements Comparable<Achievement> {

    public enum AchievementCategory {GENERAL, FRIENDS, PARTY, TIME, LOYALTY, EXPERIENCE, GAME, LOBBY, NA}

    private final int achievementId;
    private final String name;
    private final String description;
    private final String rewards;
    private final boolean visible;
    private final boolean locked;
    private final AchievementCategory category;
    private final int gameId;

    public Achievement(int achievementId, String name, String description, String rewards, boolean visible, boolean locked, AchievementCategory category) {
        this.achievementId = achievementId;
        this.name = name;
        this.description = description;
        this.rewards = rewards;
        this.visible = visible;
        this.locked = locked;
        this.category = category;
        this.gameId = -1;
    }

    public Achievement(int achievementId, String name, String description, String rewards, boolean visible, boolean locked, AchievementCategory category, int gameId) {
        this.achievementId = achievementId;
        this.name = name;
        this.description = description;
        this.rewards = rewards;
        this.visible = visible;
        this.locked = locked;
        this.category = category;
        this.gameId = gameId;
    }

    public String getName() {
        return name;
    }

    public Integer getAchievementId() {
        return achievementId;
    }

    public String getDescription() {
        return description;
    }

    public String getRewards() {
        return rewards;
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean isVisible() {
        return visible;
    }

    public AchievementCategory getCategory() {
        return category;
    }

    public int getGameId() {
        return gameId;
    }

    @Override
    public int compareTo(Achievement o) {
        return this.getAchievementId().compareTo(o.getAchievementId());
    }
}
