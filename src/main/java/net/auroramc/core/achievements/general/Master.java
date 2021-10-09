/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.general;

import net.auroramc.core.api.stats.Achievement;

public class Master extends Achievement {
    public Master() {
        super(3, "Master Player", "Purchase Master Rank.", "None", true, false, AchievementCategory.GENERAL);
    }
}
