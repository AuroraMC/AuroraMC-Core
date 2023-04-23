/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.achievements.game.crystalquest;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class Unlucky extends Achievement {

    public Unlucky() {
        super(63, "Unlucky", "Lose a game of Crystal Quest after capturing all 3 Enemy Crystals.", new Reward("&a+1000 XP\n&6+1000 Crowns", 1000, 0, 1000, Collections.emptyMap(), Collections.emptyList()), true, true, AchievementCategory.GAME);
    }
}
