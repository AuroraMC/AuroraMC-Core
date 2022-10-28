/*
 * Copyright (c) 2021-2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.lobby;

import net.auroramc.core.api.stats.Achievement;

public class WhoopsMyBad extends Achievement {
    public WhoopsMyBad() {
        super(207, "Whoops my bad", "While in a parkour, run over a pressure plate of a different parkour.", "None", true, true, AchievementCategory.LOBBY);
    }
}
