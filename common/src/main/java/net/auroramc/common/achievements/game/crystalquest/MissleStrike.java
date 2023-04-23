/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.achievements.game.crystalquest;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class MissleStrike extends Achievement {


    public MissleStrike() {
        super(75, "Missle Strike", "Die from a Crystal Explosion.", new Reward("&a+1500 XP\n&6+1500 Crowns", 1500, 0, 1500, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.GAME);
    }
}
