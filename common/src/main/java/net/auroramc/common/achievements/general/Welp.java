/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.achievements.general;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class Welp extends Achievement {
    public Welp() {
        super(20, "Welp", "Lose a game for the first time", new Reward("&a+100 XP\n&6+100 Crowns", 100, 0, 100, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.GENERAL);
    }
}
