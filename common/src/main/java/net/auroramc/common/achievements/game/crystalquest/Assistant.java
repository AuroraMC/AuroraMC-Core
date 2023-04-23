/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.achievements.game.crystalquest;

import net.auroramc.api.stats.AchievementTier;
import net.auroramc.api.utils.Reward;
import net.auroramc.api.stats.TieredAcheivement;

import java.util.Collections;

public class Assistant extends TieredAcheivement {

    public Assistant() {
        super(74, "Assistant", "Assist %s kills.", new Reward("None", 0, 0, 0, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.GAME);
        this.setTier(1, new AchievementTier(this, 1, 50, new Reward("&a+1000 XP\n&6+1000 Crowns", 1000, 0, 1000, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(2, new AchievementTier(this, 2, 250, new Reward("&a+2500 XP\n&6+2500 Crowns", 2500, 0, 2500, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(3, new AchievementTier(this, 3, 500, new Reward("&a+5000 XP\n&6+5000 Crowns", 5000, 0, 5000, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(4, new AchievementTier(this, 4, 1000, new Reward("&a+10000 XP\n&6+10000 Crowns", 10000, 0, 10000, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(5, new AchievementTier(this, 5, 2000, new Reward("&a+25000 XP\n&6+25000 Crowns", 25000, 0, 25000, Collections.emptyMap(), Collections.emptyList())));
    }
}
