/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.game.crystalquest;

import net.auroramc.core.api.stats.Achievement;

public class Unlucky extends Achievement {
    public Unlucky() {
        super(63, "Unlucky", "Lose a game of Crystal Quest after capturing all 3 Enemy Crystals.", "None", true, true, AchievementCategory.GAME, 1);
    }
}
