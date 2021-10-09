/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.party;

import net.auroramc.core.api.stats.Achievement;

public class PartyAllNight extends Achievement {
    public PartyAllNight() {
        super(40, "Party all night!", "Be in the same party for 3 hours", "None", true, false, AchievementCategory.PARTY);
    }
}
