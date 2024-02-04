/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.achievements.game.ffa;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class TtttttripleKill extends Achievement {


    public TtttttripleKill() {
        super(150, "T-t-t-t-t-triple Kill", "Kill 3 players within the first minute of the game.", new Reward("&a+2500 XP\n&6+2500 Crowns", 2500, 0, 2500, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.GAME, 102);
    }
}
