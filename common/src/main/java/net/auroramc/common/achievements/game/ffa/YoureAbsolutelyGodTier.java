/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
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
