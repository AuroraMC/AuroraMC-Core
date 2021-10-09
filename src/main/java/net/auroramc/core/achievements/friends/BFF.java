/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.friends;

import net.auroramc.core.api.stats.Achievement;

public class BFF extends Achievement {
    public BFF() {
        super(33, "BFF", "Mark someone as a favourite friend", "None", true, true, AchievementCategory.FRIENDS);
    }
}
