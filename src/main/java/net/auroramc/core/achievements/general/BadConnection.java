/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.general;

import net.auroramc.core.api.stats.Achievement;

public class BadConnection extends Achievement {
    public BadConnection() {
        super(23, "Bad Connection", "Leave and rejoin 5 times in 15 minutes", "None", true, false, AchievementCategory.GENERAL);
    }
}