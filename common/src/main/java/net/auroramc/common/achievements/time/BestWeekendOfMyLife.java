/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.achievements.time;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class BestWeekendOfMyLife extends Achievement {
    public BestWeekendOfMyLife() {
        super(47, "Best weekend of my life", "Spend 48 hours on AuroraMC", new Reward("&a+25000 XP\n&d+25000 Tickets", 25000, 25000, 0, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.TIME);
    }
}
