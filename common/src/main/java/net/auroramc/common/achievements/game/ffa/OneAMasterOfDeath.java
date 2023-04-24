/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.achievements.game.ffa;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class OneAMasterOfDeath extends Achievement {


    public OneAMasterOfDeath() {
        super(148, "One, a master of Death", "Die 100 times in FFA.", new Reward("&a+2500 XP\n&6+2500 Crowns", 2500, 0, 2500, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.GAME, 102);
    }
}
