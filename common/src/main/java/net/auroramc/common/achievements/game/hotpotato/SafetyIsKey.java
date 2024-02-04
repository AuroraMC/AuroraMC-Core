/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.achievements.game.hotpotato;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class SafetyIsKey extends Achievement {


    public SafetyIsKey() {
        super(162, "Safety is key...", "Survive a round without receiving the Hot Potato.", new Reward("&a+250 XP\n&6+250 Crowns", 250, 0, 250, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.GAME, 101);
    }
}
