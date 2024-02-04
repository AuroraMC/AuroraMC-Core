/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.achievements.game.spleef;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.stats.AchievementTier;
import net.auroramc.api.utils.Reward;
import net.auroramc.api.stats.TieredAcheivement;

import java.util.Collections;

public class AnArmLikeSnowbodyElse extends TieredAcheivement {
    public AnArmLikeSnowbodyElse() {
        super(121, "An Arm Like Snowbody Else", "Throw %s Snowballs", new Reward("None", 0, 0, 0, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.GAME, 100);
        this.setTier(1, new AchievementTier(this, 1, 50, new Reward("&a+500 XP\n&6+500 Crowns", 500, 0, 500, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(2, new AchievementTier(this, 2, 250, new Reward("&a+750 XP\n&6+750 Crowns", 750, 0, 750, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(3, new AchievementTier(this, 3, 500, new Reward("&a+1000 XP\n&6+1000 Crowns", 1000, 0, 1000, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(4, new AchievementTier(this, 4, 1000, new Reward("&a+2500 XP\n&6+2500 Crowns", 2500, 0, 2500, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(5, new AchievementTier(this, 5, 2000, new Reward("&a+5000 XP\n&6+5000 Crowns", 5000, 0, 5000, Collections.emptyMap(), Collections.emptyList())));
    }
}
