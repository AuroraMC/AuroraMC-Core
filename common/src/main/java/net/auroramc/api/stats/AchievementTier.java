/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.api.stats;

import net.auroramc.api.utils.Reward;

public class AchievementTier {

    private final TieredAcheivement acheivement;
    private final int tier;
    private final long requirement;
    private final Reward reward;

    public AchievementTier(TieredAcheivement acheivement, int tier, long requirement, Reward reward) {
        this.acheivement = acheivement;
        this.tier = tier;
        this.requirement = requirement;
        this.reward = reward;
    }

    public long getRequirement() {
        return requirement;
    }

    public int getTier() {
        return tier;
    }

    public TieredAcheivement getAcheivement() {
        return acheivement;
    }

    public Reward getReward() {
        return reward;
    }
}
