/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.party;

import net.auroramc.core.api.stats.Achievement;

public class PartyAnimal extends Achievement {
    public PartyAnimal() {
        super(39, "Party Animal", "Have 0 slots left in your party.", "None", true, false, AchievementCategory.PARTY);
    }
}
