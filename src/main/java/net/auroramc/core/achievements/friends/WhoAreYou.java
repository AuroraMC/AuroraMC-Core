/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.friends;

import net.auroramc.core.api.stats.Achievement;

public class WhoAreYou extends Achievement {
    public WhoAreYou() {
        super(32, "Who are you?", "Deny a friend request", "None", true, true, AchievementCategory.FRIENDS);
    }
}
