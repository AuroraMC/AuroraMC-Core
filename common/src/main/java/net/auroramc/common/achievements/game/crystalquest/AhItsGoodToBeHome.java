/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.achievements.game.crystalquest;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class AhItsGoodToBeHome extends Achievement {


    public AhItsGoodToBeHome() {
        super(66, "Ah, it's good to be home", "Kill a player who has collected on of your Crystals.", new Reward("&a+500 XP\n&6+500 Crowns", 500, 0, 500, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.GAME, 1);
    }
}
