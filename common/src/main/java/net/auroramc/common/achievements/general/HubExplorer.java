/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.achievements.general;

import net.auroramc.api.stats.AchievementTier;
import net.auroramc.api.stats.TieredAcheivement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class HubExplorer extends TieredAcheivement {
    public HubExplorer () {
        super(11, "Hub Explorer", "Spend %s hours in hub", new Reward("None", 0, 0, 0, Collections.emptyMap(), Collections.emptyList()), true, true, AchievementCategory.GENERAL);
        this.setTier(1, new AchievementTier(this, 1, 24, new Reward("&a+500 XP\n&d+500 Tickets", 500, 500, 0, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(2, new AchievementTier(this, 2, 48, new Reward("&a+1000 XP\n&d+1000 Tickets", 1000, 1000, 0, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(3, new AchievementTier(this, 3, 168, new Reward("&a+2500 XP\n&d+2500 Tickets", 2500, 2500, 0, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(4, new AchievementTier(this, 4, 504, new Reward("&a+5000 XP\n&d+5000 Tickets", 5000, 5000, 0, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(5, new AchievementTier(this, 5, 744, new Reward("&a+10000 XP\n&d+10000 Tickets", 10000, 10000, 0, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(6, new AchievementTier(this, 6, 1440, new Reward("&a+25000 XP\n&d+25000 Tickets", 25000, 25000, 0, Collections.emptyMap(), Collections.emptyList())));
    }
}
