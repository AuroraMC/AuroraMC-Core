/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
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
