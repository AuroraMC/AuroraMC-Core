/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.achievements.game.crystalquest;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class YouCantSeeMe extends Achievement {


    public YouCantSeeMe() {
        super(72, "You can't see me!", "Kill a player without losing any health points.", new Reward("&a+1000 XP\n&6+1000 Crowns", 1000, 0, 1000, Collections.emptyMap(), Collections.emptyList()), true, true, AchievementCategory.GAME);
    }
}