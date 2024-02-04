/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.achievements.game.ffa;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class CliffJumper extends Achievement {


    public CliffJumper() {
        super(146, "Cliff Jumper", "Somehow... if you manage to... die to fall damage.", new Reward("&a+10000 XP\n&6+5000 Crowns", 10000, 0, 5000, Collections.emptyMap(), Collections.emptyList()), true, true, AchievementCategory.GAME, 102);
    }
}
