/*
 * Copyright (c) 2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.achievements.general;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class HeyStopThat extends Achievement {
    public HeyStopThat() {
        super(5, "Hey! Stop that!", "Attempt to break a block that is protected.", new Reward("&a+100 XP\n&d+100 Tickets", 100, 100, 0, Collections.emptyMap(), Collections.emptyList()), true, true, AchievementCategory.GENERAL);
    }
}
