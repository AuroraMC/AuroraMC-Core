/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.achievements.friends;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class BFF extends Achievement {
    public BFF() {
        super(33, "BFF", "Mark someone as a favourite friend", new Reward("&a+1000 XP\n&6+1000 Crowns", 1000, 0, 1000, Collections.emptyMap(), Collections.emptyList()), true, true, AchievementCategory.FRIENDS);
    }
}
