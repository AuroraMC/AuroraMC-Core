/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.achievements.game.ffa;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class GetBackToWork extends Achievement {


    public GetBackToWork() {
        super(144, "Get back to work!", "Kill a member of the AuroraMC Moderation Team.", new Reward("&a+2500 XP\n&6+2500 Crowns", 2500, 0, 2500, Collections.emptyMap(), Collections.emptyList()), true, true, AchievementCategory.GAME, 102);
    }
}
