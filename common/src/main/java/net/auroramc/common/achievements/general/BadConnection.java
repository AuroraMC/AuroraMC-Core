/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.achievements.general;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class BadConnection extends Achievement {
    public BadConnection() {
        super(23, "Bad Connection", "Leave and rejoin 5 times in 15 minutes", new Reward("&a+500 XP", 500, 0, 0, Collections.emptyMap(), Collections.emptyList()), false, false, AchievementCategory.GENERAL);
    }
}