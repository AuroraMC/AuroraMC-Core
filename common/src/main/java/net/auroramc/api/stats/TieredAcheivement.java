/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.api.stats;

import net.auroramc.api.utils.Reward;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TieredAcheivement extends Achievement {

    private final Map<Integer, AchievementTier> tiers;

    public TieredAcheivement(int achievementId, String name, String description, Reward rewards, boolean visible, boolean locked, AchievementCategory category) {
        super(achievementId, name, description, rewards, visible, locked, category);

        this.tiers = new HashMap<>();
    }

    public TieredAcheivement(int achievementId, String name, String description, Reward rewards, boolean visible, boolean locked, AchievementCategory category, int gameId) {
        super(achievementId, name, description, rewards, visible, locked, category, gameId);

        this.tiers = new HashMap<>();
    }

    public List<AchievementTier> getTiers() {
        return new ArrayList<>(tiers.values());
    }

    public int achievedTier(int currentTier, long metric) {
        if (tiers.size() > currentTier) {
            if (metric >= tiers.get(currentTier + 1).getRequirement()) {
                return currentTier + 1;
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    public AchievementTier getTier(int tier) {
        return tiers.get(tier);
    }

    public void setTier(int tier, AchievementTier achievementTier) {
        tiers.put(tier, achievementTier);
    }

    public Reward getReward(int tier) {
        if (!tiers.containsKey(tier)) {
            return null;
        }
        return tiers.get(tier).getReward();
    }
}
