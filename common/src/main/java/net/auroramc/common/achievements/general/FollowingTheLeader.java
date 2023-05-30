/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.achievements.general;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

import net.auroramc.api.stats.Achievement;

public class FollowingTheLeader extends Achievement {
    public FollowingTheLeader() {
        super(26, "Following the Leader", "Be in the same lobby as a member of the Leadership Team", new Reward("&a+2000 XP\n&d+2000 Tickets", 2000, 2000, 0, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.GENERAL);
    }
}
