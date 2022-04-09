/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.game.crystalquest;

import net.auroramc.core.api.stats.Achievement;

public class MissleStrike extends Achievement {
    public MissleStrike() {
        super(75, "Missle Strike", "Die from a Crystal Explosion.", "None", true, true, AchievementCategory.GAME, 1);
    }
}
