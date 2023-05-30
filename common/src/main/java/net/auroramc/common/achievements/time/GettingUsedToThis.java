/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.achievements.time;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class GettingUsedToThis extends Achievement {
    public GettingUsedToThis() {
        super(43, "Getting used to this", "Spend 1 hour on AuroraMC", new Reward("&a+1000 XP\n&d+1000 Tickets", 1000, 1000, 0, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.TIME);
    }
}