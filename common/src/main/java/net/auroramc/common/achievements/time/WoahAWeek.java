/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.achievements.time;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class WoahAWeek extends Achievement {
    public WoahAWeek() {
        super(48, "Woah a week?", "Spend a week on AuroraMC", new Reward("&a+50000 XP\n&d+50000 Tickets", 50000, 50000, 0, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.TIME);
    }
}
