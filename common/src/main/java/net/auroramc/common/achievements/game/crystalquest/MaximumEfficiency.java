/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.achievements.game.crystalquest;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class MaximumEfficiency extends Achievement {


    public MaximumEfficiency() {
        super(69, "Maximum Efficiency", "Upgrade a Mining Robot to Level 3.", new Reward("&a+2500 XP\n&6+2500 Crowns", 2500, 0, 2500, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.GAME, 1);
    }
}
