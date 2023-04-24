/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.achievements.game.hotpotato;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class WhoLeftTheOvenOn extends Achievement {


    public WhoLeftTheOvenOn() {
        super(163, "Who left the oven on?", "Receive a Hot Potato every round and win the game.", new Reward("&a+10000 XP\n&6+10000 Crowns", 10000, 0, 10000, Collections.emptyMap(), Collections.emptyList()), true, true, AchievementCategory.GAME, 101);
    }
}
