/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.party;

import net.auroramc.core.api.stats.Achievement;

public class Late extends Achievement {
    public Late() {
        super(38, "Late", "Join a party that already has 5 people in it", "None", true, true, AchievementCategory.PARTY);
    }
}
