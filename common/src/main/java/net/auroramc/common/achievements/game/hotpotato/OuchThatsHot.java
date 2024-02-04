/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.achievements.game.hotpotato;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class OuchThatsHot extends Achievement {


    public OuchThatsHot() {
        super(164, "Ouch, that's hot!", "Die from receiving the hot potato in the last 3 seconds of the round.", new Reward("&a+1000 XP\n&6+1000 Crowns", 1000, 0, 1000, Collections.emptyMap(), Collections.emptyList()), true, true, AchievementCategory.GAME, 101);
    }
}
