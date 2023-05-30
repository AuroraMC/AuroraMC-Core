/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.achievements.time;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class IThinkILikeThis extends Achievement {
    public IThinkILikeThis() {
        super(44, "I think I like this", "Spend 5 hours on AuroraMC", new Reward("&a+2500 XP\n&d+2500 Tickets", 2500, 2500, 0, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.TIME);
    }
}
