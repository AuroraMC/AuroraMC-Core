/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.achievements.game.hotpotato;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class SafetyIsKey extends Achievement {


    public SafetyIsKey() {
        super(162, "Safety is key...", "Survive a round without receiving the Hot Potato.", new Reward("&a+250 XP\n&6+250 Crowns", 250, 0, 250, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.GAME, 101);
    }
}
