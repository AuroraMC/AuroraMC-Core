/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.party;

import net.auroramc.core.api.stats.Achievement;

public class ImComingToo extends Achievement {
    public ImComingToo() {
        super(36, "I'm coming too!", "Join another user's party", "None", true, false, AchievementCategory.PARTY);
    }
}
