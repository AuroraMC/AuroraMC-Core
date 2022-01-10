/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.stats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TieredAcheivement extends Achievement {

    private final Map<Integer, AchievementTier> tiers;

    public TieredAcheivement(int achievementId, String name, String description, String rewards, boolean visible, boolean locked, AchievementCategory category) {
        super(achievementId, name, description, rewards, visible, locked, category);

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

    public void setTier(int tier, AchievementTier achievementTier) {
        tiers.put(tier, achievementTier);
    }
}
