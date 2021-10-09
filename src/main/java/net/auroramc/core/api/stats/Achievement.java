/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.stats;

public abstract class Achievement implements Comparable<Achievement> {

    public enum AchievementCategory {GENERAL, FRIENDS, PARTY, TIME, LOYALTY, EXPERIENCE, GAME, NA}

    private final int achievementId;
    private final String name;
    private final String description;
    private final String rewards;
    private final boolean visible;
    private final boolean locked;
    private final AchievementCategory category;

    public Achievement(int achievementId, String name, String description, String rewards, boolean visible, boolean locked, AchievementCategory category) {
        this.achievementId = achievementId;
        this.name = name;
        this.description = description;
        this.rewards = rewards;
        this.visible = visible;
        this.locked = locked;
        this.category = category;
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

    @Override
    public int compareTo(Achievement o) {
        return this.getAchievementId().compareTo(o.getAchievementId());
    }
}
