/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.game.crystalquest;

import net.auroramc.core.api.stats.Achievement;

public class Failure extends Achievement {
    public Failure() {
        super(64, "Failure", "Fail to capture an Enemy Crystal after collecting it from their tower.", "None", true, true, AchievementCategory.GAME, 1);
    }
}
