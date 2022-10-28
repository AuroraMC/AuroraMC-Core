/*
 * Copyright (c) 2021-2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.lobby;

import net.auroramc.core.api.stats.Achievement;

public class TooEasyForMe extends Achievement {
    public TooEasyForMe() {
        super(204, "Too Easy for Me", "Achieve a time of less than 1 minute 30 seconds on the Easy Parkour!", "None", true, true, AchievementCategory.LOBBY);
    }
}
