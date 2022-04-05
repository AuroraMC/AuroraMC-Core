/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.game.spleef;

import net.auroramc.core.api.stats.AchievementTier;
import net.auroramc.core.api.stats.TieredAcheivement;

public class StraightUpBallin extends TieredAcheivement {
    public StraightUpBallin() {
        super(122, "Straight Up Ballin'!", "Win %s Spleef Games", "None", true, false, AchievementCategory.GAME, 100);
        this.setTier(1, new AchievementTier(this, 1, 50));
        this.setTier(2, new AchievementTier(this, 2, 250));
        this.setTier(3, new AchievementTier(this, 3, 500));
        this.setTier(4, new AchievementTier(this, 4, 1000));
        this.setTier(5, new AchievementTier(this, 5, 2000));
    }
}
