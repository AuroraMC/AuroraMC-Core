/*
 * Copyright (c) 2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.achievements.friends;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class NotALoner extends Achievement {
    public NotALoner() {
        super(30, "Not a loner", "Make your first friend!", new Reward("&a+100 XP\n&6+100 Crowns", 100, 0, 100, Collections.emptyMap(), Collections.emptyList()), true, true, AchievementCategory.FRIENDS);
    }
}