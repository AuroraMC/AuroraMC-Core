/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.achievements.general;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

import net.auroramc.api.stats.AchievementTier;
import net.auroramc.api.stats.TieredAcheivement;

public class GettingTheHangOfThis extends TieredAcheivement {
    public GettingTheHangOfThis() {
        super(9, "Getting the hang of this", "Play %s games on AuroraMC", new Reward("None", 0, 0, 0, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.GENERAL);
        this.setTier(1, new AchievementTier(this, 1, 10, new Reward("&a+1000 XP\n&6+1000 Crowns", 1000, 0, 1000, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(2, new AchievementTier(this, 2, 100, new Reward("&a+2500 XP\n&6+2500 Crowns", 2500, 0, 2500, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(3, new AchievementTier(this, 3, 1000, new Reward("&a+5000 XP\n&6+5000 Crowns", 5000, 0, 5000, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(4, new AchievementTier(this, 4, 10000, new Reward("&a+10000 XP\n&6+10000 Crowns", 10000, 0, 10000, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(5, new AchievementTier(this, 5, 100000, new Reward("&a+25000 XP\n&6+25000 Crowns", 25000, 0, 25000, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(6, new AchievementTier(this, 6, 1000000, new Reward("&a+50000 XP\n&6+50000 Crowns", 50000, 0, 50000, Collections.emptyMap(), Collections.emptyList())));
    }
}
