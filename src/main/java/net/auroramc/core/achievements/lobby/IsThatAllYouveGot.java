/*
 * Copyright (c) 2021-2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.lobby;

import net.auroramc.core.api.stats.Achievement;

public class IsThatAllYouveGot extends Achievement {
    public IsThatAllYouveGot() {
        super(206, "Is that all you've got?", "Achieve a time of less than 4 minutes on the Hard Parkour!", "None", true, true, AchievementCategory.LOBBY);
    }
}
