/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.time;

import net.auroramc.core.api.stats.Achievement;

public class MyNewHome extends Achievement {
    public MyNewHome() {
        super(45, "My new home", "Spend 10 hours on AuroraMC", ";&a5000 XP;&d5000 Tickets", true, false, AchievementCategory.TIME);
    }
}
