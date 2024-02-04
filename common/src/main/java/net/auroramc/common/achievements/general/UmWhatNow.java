/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.achievements.general;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class UmWhatNow extends Achievement {
    public UmWhatNow() {
        super(27, "Um whatnow?", "Attempt to use a command that doesn't exist.", new Reward("&6+500 XP\n&6+500 Crowns", 500, 0, 500, Collections.emptyMap(), Collections.emptyList()), true, true, AchievementCategory.GENERAL);
    }
}
