/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.experience;

import net.auroramc.core.api.stats.Achievement;

public class God extends Achievement {
    public God() {
        super(60, "GOD", "Reach level 200", "None", true, false, AchievementCategory.EXPERIENCE);
    }
}
