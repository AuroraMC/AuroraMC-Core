/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.achievements.experience;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class Beast extends Achievement {
    public Beast() {
        super(59, "BEAST", "Reach level 150", new Reward("None", 0, 0, 0, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.EXPERIENCE);
    }
}
