/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.achievements.game.ffa;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class TheUselessUpgrade extends Achievement {


    public TheUselessUpgrade() {
        super(149, "The Useless Upgrade", "Reach FFA Kit Level 100.", new Reward("&a+5000 XP\n&6+5000 Crowns", 5000, 0, 5000, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.GAME, 102);
    }
}
