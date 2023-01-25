/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.general;

import net.auroramc.core.api.stats.Achievement;

public class Welp extends Achievement {
    public Welp() {
        super(20, "Welp", "Lose a game for the first time", ";&a100 XP;&6100 Crowns", true, false, AchievementCategory.GENERAL);
    }
}
