/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.general;

import net.auroramc.core.api.stats.Achievement;

public class Welcome extends Achievement {
    public Welcome() {
        super(0, "Welcome to AuroraMC", "Join the network for the first time!", ";&a100 XP;&6100 Crowns", true, false, AchievementCategory.GENERAL);
    }
}
