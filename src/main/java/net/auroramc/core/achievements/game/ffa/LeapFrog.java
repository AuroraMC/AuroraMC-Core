/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.game.ffa;

import net.auroramc.core.api.stats.AchievementTier;
import net.auroramc.core.api.stats.TieredAcheivement;

public class LeapFrog extends TieredAcheivement {
    public LeapFrog() {
        super(147, "Leap Frog", "Leap %s times in FFA.", "None", true, false, AchievementCategory.GAME, 102);
        this.setTier(1, new AchievementTier(this, 1, 10));
        this.setTier(2, new AchievementTier(this, 2, 50));
        this.setTier(3, new AchievementTier(this, 3, 100));
        this.setTier(4, new AchievementTier(this, 4, 200));
        this.setTier(5, new AchievementTier(this, 5, 500));
        this.setTier(6, new AchievementTier(this, 5, 1000));
    }
}
