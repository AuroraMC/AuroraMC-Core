/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.game.crystalquest;

import net.auroramc.core.api.stats.Achievement;

public class YouCantSeeMe extends Achievement {
    public YouCantSeeMe() {
        super(72, "You can't see me!", "Kill a player without losing any health points.", "None", true, true, AchievementCategory.GAME, 1);
    }
}
