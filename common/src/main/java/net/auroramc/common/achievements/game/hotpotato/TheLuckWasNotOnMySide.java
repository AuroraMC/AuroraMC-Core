/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.achievements.game.hotpotato;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class TheLuckWasNotOnMySide extends Achievement {


    public TheLuckWasNotOnMySide() {
        super(171, "The luck was not on my side", "Die to the Hot Potato after receiving it at random and failing to lose it.", new Reward("&a+1000 XP\n&6+1000 Crowns", 1000, 0, 1000, Collections.emptyMap(), Collections.emptyList()), true, true, AchievementCategory.GAME);
    }
}
