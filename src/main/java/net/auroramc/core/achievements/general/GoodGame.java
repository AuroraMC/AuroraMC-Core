/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.general;

import net.auroramc.core.api.stats.Achievement;

public class GoodGame extends Achievement {
    public GoodGame() {
        super(8, "Good Game", "Play your first game on AuroraMC", "None", true, true, AchievementCategory.GENERAL);
    }
}