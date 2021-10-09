/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.party;

import net.auroramc.core.api.stats.Achievement;

public class YoureComingWithMe extends Achievement {
    public YoureComingWithMe() {
        super(37, "You're coming with me", "Move everyone in your party to a new server", "None", true, false, AchievementCategory.PARTY);
    }
}
