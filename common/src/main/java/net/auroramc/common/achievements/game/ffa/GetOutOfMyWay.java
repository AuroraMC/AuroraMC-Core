/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.achievements.game.ffa;

import net.auroramc.api.stats.AchievementTier;
import net.auroramc.api.utils.Reward;
import net.auroramc.api.stats.TieredAcheivement;

import java.util.Collections;

public class GetOutOfMyWay extends TieredAcheivement {

    public GetOutOfMyWay() {
        super(141, "Get out of my way!", "Break %s blocks.", new Reward("None", 0, 0, 0, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.GAME, 102);
        this.setTier(1, new AchievementTier(this, 1, 100, new Reward("&a+500 XP\n&6+500 Crowns", 500, 0, 500, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(2, new AchievementTier(this, 2, 250, new Reward("&a+1000 XP\n&6+1000 Crowns", 1000, 0, 1000, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(3, new AchievementTier(this, 3, 750, new Reward("&a+2500 XP\n&6+2500 Crowns", 2500, 0, 2500, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(4, new AchievementTier(this, 4, 1500, new Reward("&a+5000 XP\n&6+5000 Crowns", 5000, 0, 5000, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(5, new AchievementTier(this, 5, 3000, new Reward("&a+10000 XP\n&6+10000 Crowns", 10000, 0, 10000, Collections.emptyMap(), Collections.emptyList())));
    }
}
