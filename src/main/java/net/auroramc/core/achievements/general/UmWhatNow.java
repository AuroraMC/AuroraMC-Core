/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.general;

import net.auroramc.core.api.stats.Achievement;

public class UmWhatNow extends Achievement {
    public UmWhatNow() {
        super(27, "Um whatnow?", "Attempt to use a command that doesn't exist.", "None", true, true, AchievementCategory.GENERAL);
    }
}
