/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.party;

import net.auroramc.core.api.stats.Achievement;

public class Awkward extends Achievement {
    public Awkward() {
        super(41, "Awkward...", "Leave a party to join a different one.", "None", true, false, AchievementCategory.PARTY);
    }
}
