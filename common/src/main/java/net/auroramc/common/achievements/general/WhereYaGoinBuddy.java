/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.achievements.general;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class WhereYaGoinBuddy extends Achievement {
    public WhereYaGoinBuddy() {
        super(7, "Where ya goin' buddy?", "Attempt to join a server you don't have access to", new Reward("&a+500 XP\n&6+500 Crowns", 500, 0, 500, Collections.emptyMap(), Collections.emptyList()), true, true, AchievementCategory.GENERAL);
    }
}
