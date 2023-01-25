/*
 * Copyright (c) 2021-2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.lobby;

import net.auroramc.core.api.stats.Achievement;

public class TooEasyForMe extends Achievement {
    public TooEasyForMe() {
        super(204, "Too Easy for Me", "Achieve a time of less than 30 seconds on the Easy Parkour!", ";&a1000 XP;&61000 Crowns", true, true, AchievementCategory.LOBBY);
    }
}
