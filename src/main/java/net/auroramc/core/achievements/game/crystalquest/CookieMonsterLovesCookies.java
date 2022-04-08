/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.game.crystalquest;

import net.auroramc.core.api.stats.AchievementTier;
import net.auroramc.core.api.stats.TieredAcheivement;

public class CookieMonsterLovesCookies extends TieredAcheivement {

    public CookieMonsterLovesCookies() {
        super(73, "COOKIE MONSTER LOVES COOKIES", "Eat %s Instant Cookies.", "None", true, false, AchievementCategory.GAME, 1);
        this.setTier(1, new AchievementTier(this, 1, 1));
        this.setTier(2, new AchievementTier(this, 2, 10));
        this.setTier(3, new AchievementTier(this, 3, 50));
        this.setTier(4, new AchievementTier(this, 4, 100));
        this.setTier(5, new AchievementTier(this, 5, 200));
    }
}
