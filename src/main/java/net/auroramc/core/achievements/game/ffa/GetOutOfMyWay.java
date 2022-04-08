/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.game.ffa;

import net.auroramc.core.api.stats.AchievementTier;
import net.auroramc.core.api.stats.TieredAcheivement;

public class GetOutOfMyWay extends TieredAcheivement {

    public GetOutOfMyWay() {
        super(141, "Get our of my way!", "Break %s blocks.", "None", true, false, AchievementCategory.GAME, 102);
        this.setTier(1, new AchievementTier(this, 1, 100));
        this.setTier(2, new AchievementTier(this, 2, 250));
        this.setTier(3, new AchievementTier(this, 3, 750));
        this.setTier(4, new AchievementTier(this, 4, 1500));
        this.setTier(5, new AchievementTier(this, 5, 3000));
    }
}
