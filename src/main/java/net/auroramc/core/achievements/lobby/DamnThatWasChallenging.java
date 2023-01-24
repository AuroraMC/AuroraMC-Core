/*
 * Copyright (c) 2021-2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.lobby;

import net.auroramc.core.api.stats.Achievement;

public class DamnThatWasChallenging extends Achievement {
    public DamnThatWasChallenging() {
        super(205, "Damn, that was challenging!", "Achieve a time of less than 2 minutes 30 seconds on the Medium Parkour!", ";&a7500 XP;&67500 Crowns", true, true, AchievementCategory.LOBBY);
    }
}
