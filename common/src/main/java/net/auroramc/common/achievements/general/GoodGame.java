/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.achievements.general;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class GoodGame extends Achievement {
    public GoodGame() {
        super(8, "Good Game", "Play your first game on AuroraMC", new Reward("&a+500 XP\n&6+1000 Crowns", 500, 0, 1000, Collections.emptyMap(), Collections.emptyList()), true, true, AchievementCategory.GENERAL);
    }
}