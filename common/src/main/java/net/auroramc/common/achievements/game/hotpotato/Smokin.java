/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.achievements.game.hotpotato;

import net.auroramc.api.stats.AchievementTier;
import net.auroramc.api.utils.Reward;
import net.auroramc.api.stats.TieredAcheivement;

import java.util.Collections;

public class Smokin extends TieredAcheivement {

    public Smokin() {
        super(168, "SMOKIN'", "Die to the Hot Potato %s times.", new Reward("None", 0, 0, 0, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.GAME);
        this.setTier(1, new AchievementTier(this, 1, 10, new Reward("&a+500 XP\n&6+500 Crowns", 500, 0, 500, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(2, new AchievementTier(this, 2, 50, new Reward("&a+750 XP\n&6+750 Crowns", 750, 0, 750, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(3, new AchievementTier(this, 3, 100, new Reward("&a+1000 XP\n&6+1000 Crowns", 1000, 0, 1000, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(4, new AchievementTier(this, 4, 200, new Reward("&a+2500 XP\n&6+2500 Crowns", 2500, 0, 2500, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(5, new AchievementTier(this, 5, 500, new Reward("&a+5000 XP\n&6+5000 Crowns", 5000, 0, 5000, Collections.emptyMap(), Collections.emptyList())));
    }
}
