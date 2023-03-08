/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.game.crystalquest;

import net.auroramc.core.api.stats.Achievement;

public class YouDidIt extends Achievement {
    public YouDidIt() {
        super(61, "You Did It!", "Win a game of Crystal Quest for the first time.", ";&a1000 XP;&61000 Crowns", true, false, AchievementCategory.GAME, 1);
    }
}
