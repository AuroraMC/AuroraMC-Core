/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.game.crystalquest;

import net.auroramc.core.api.stats.AchievementTier;
import net.auroramc.core.api.stats.TieredAcheivement;

public class KillHungry extends TieredAcheivement {

    public KillHungry() {
        super(65, "Kill Hungry", "Kill %s Enemies in Crystal Quest.", "None", true, false, AchievementCategory.GAME, 1);
        this.setTier(1, new AchievementTier(this, 1, 50));
        this.setTier(2, new AchievementTier(this, 2, 250));
        this.setTier(3, new AchievementTier(this, 3, 500));
        this.setTier(4, new AchievementTier(this, 4, 1000));
        this.setTier(5, new AchievementTier(this, 5, 2000));
    }
}
