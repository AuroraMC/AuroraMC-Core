/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.general;

import net.auroramc.core.api.stats.Achievement;

public class HeyStopThat extends Achievement {
    public HeyStopThat() {
        super(5, "Hey! Stop that!", "Attempt to break a block that is protected.", ";&a100 XP;&d100 Tickets", true, true, AchievementCategory.GENERAL);
    }
}
