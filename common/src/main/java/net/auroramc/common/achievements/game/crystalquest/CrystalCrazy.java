/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.achievements.game.crystalquest;

import net.auroramc.api.stats.AchievementTier;
import net.auroramc.api.utils.Reward;
import net.auroramc.api.stats.TieredAcheivement;

import java.util.Collections;

public class CrystalCrazy extends TieredAcheivement {

    public CrystalCrazy() {
        super(62, "Crystal Crazy", "Return %s Enemy Crystals to your team's base.", new Reward("None", 0, 0, 0, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.GAME, 1);
        this.setTier(1, new AchievementTier(this, 1, 50, new Reward("&a+5000 XP\n&6+5000 Crowns", 5000, 0, 5000, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(2, new AchievementTier(this, 2, 250, new Reward("&a+10000 XP\n&6+10000 Crowns", 10000, 0, 10000, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(3, new AchievementTier(this, 3, 500, new Reward("&a+25000 XP\n&6+25000 Crowns", 25000, 0, 25000, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(4, new AchievementTier(this, 4, 1000, new Reward("&a+50000 XP\n&6+50000 Crowns", 50000, 0, 50000, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(5, new AchievementTier(this, 5, 2000, new Reward("&a+100000 XP\n&6+100000 Crowns", 100000, 0, 100000, Collections.emptyMap(), Collections.emptyList())));
    }
}
