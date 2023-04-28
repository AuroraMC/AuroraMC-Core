/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.achievements.game.ffa;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class YoureAbsolutelyGodTier extends Achievement {


    public YoureAbsolutelyGodTier() {
        super(143, "You're absolutely god tier!", "Kill a member of the AuroraMC Leadership Team.", new Reward("&a+10000 XP\n&6+10000 Crowns", 10000, 0, 10000, Collections.emptyMap(), Collections.emptyList()), true, true, AchievementCategory.GAME, 102);
    }
}
