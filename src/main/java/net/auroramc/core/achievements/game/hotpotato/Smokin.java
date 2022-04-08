/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.game.hotpotato;

import net.auroramc.core.api.stats.AchievementTier;
import net.auroramc.core.api.stats.TieredAcheivement;

public class Smokin extends TieredAcheivement {

    public Smokin() {
        super(168, "SMOKIN'", "Die to the Hot Potato %s times.", "None", true, false, AchievementCategory.GAME, 101);
        this.setTier(1, new AchievementTier(this, 1, 10));
        this.setTier(2, new AchievementTier(this, 2, 50));
        this.setTier(3, new AchievementTier(this, 3, 100));
        this.setTier(4, new AchievementTier(this, 4, 200));
        this.setTier(5, new AchievementTier(this, 5, 500));
    }
}
