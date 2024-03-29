/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.achievements.game.spleef;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class AndHisNameIs extends Achievement {
    public AndHisNameIs() {
        super(124, "And his name is...", "Punch somebody out of the ring", new Reward("&a+500 XP\n&6+500 Crowns", 500, 0, 500, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.GAME, 100);
    }
}
