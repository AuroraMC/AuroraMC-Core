/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.game.crystalquest;

import net.auroramc.core.api.stats.Achievement;

public class Sniper extends Achievement {
    public Sniper() {
        super(71, "Sniper", "Kill a player with an arrow from 50 blocks away.", "None", true, false, AchievementCategory.GAME, 1);
    }
}
