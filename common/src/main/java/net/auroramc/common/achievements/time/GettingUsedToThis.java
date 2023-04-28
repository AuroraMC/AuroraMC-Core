/*
 * Copyright (c) 2021 AuroraMC Ltd. All Rights Reserved.
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