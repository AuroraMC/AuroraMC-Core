/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.friends;

import net.auroramc.core.api.stats.Achievement;

public class NotALoner extends Achievement {
    public NotALoner() {
        super(30, "Not a loner", "Make your first friend!", ";&a100 XP;&6100 Crowns", true, true, AchievementCategory.FRIENDS);
    }
}