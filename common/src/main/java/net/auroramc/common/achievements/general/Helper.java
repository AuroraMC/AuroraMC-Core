/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.achievements.general;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class Helper extends Achievement {
    public Helper() {
        super(10, "Helper", "Submit a report that gets accepted", new Reward("&a+500 XP\n&6+500 Crowns", 500, 0, 500, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.GENERAL);
    }
}
