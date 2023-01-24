/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.time;

import net.auroramc.core.api.stats.Achievement;

public class WoahAWeek extends Achievement {
    public WoahAWeek() {
        super(48, "Woah a week?", "Spend a week on AuroraMC", ";&a100000 XP;&d100000 Tickets", true, false, AchievementCategory.TIME);
    }
}
