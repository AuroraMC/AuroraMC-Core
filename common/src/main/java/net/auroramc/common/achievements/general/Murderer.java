/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.achievements.general;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class Murderer extends Achievement {
    public Murderer() {
        super(22, "Murderer!", "Kill a player for the first time", new Reward("&a+2000 XP\n&6+2000 Crowns", 2000, 0, 2000, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.GENERAL);
    }
}
