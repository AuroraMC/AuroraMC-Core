/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.achievements.general;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class Hackusator extends Achievement {
    public Hackusator() {
        super(28, "Hackusator", "Accuse a player of cheating", new Reward("&a+100 XP", 100, 0, 0, Collections.emptyMap(), Collections.emptyList()), true, true, AchievementCategory.GENERAL);
    }
}
