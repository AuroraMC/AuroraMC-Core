/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.achievements.friends;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class ThankYouNext extends Achievement {
    public ThankYouNext() {
        super(31, "Thank you, next", "Remove a friend", new Reward("&a+400 XP\n&6+400 Crowns", 400, 0, 400, Collections.emptyMap(), Collections.emptyList()), true, true, AchievementCategory.FRIENDS);
    }
}
