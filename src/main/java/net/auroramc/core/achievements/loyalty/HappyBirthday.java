/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.loyalty;

import net.auroramc.core.api.stats.Achievement;

public class HappyBirthday extends Achievement {
    public HappyBirthday() {
        super(50, "Happy Birthday", "Join AuroraMC a year (or more) after you first joined.", "None", true, false, AchievementCategory.LOYALTY);
    }
}
