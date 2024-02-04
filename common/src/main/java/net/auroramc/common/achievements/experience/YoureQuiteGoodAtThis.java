/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.achievements.experience;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class YoureQuiteGoodAtThis extends Achievement {
    public YoureQuiteGoodAtThis() {
        super(55, "You're quite good at this...", "Reach level 60", new Reward("None", 0, 0, 0, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.EXPERIENCE);
    }
}
