/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
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
