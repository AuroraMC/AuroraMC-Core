/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.general;

import net.auroramc.core.api.stats.Achievement;

public class Helper extends Achievement {
    public Helper() {
        super(10, "Helper", "Submit a report that gets accepted", ";&a500 XP;&6500 Crowns", true, false, AchievementCategory.GENERAL);
    }
}
