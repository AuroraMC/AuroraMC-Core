/*
 * Copyright (c) 2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.achievements.friends;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class WhoAreYou extends Achievement {
    public WhoAreYou() {
        super(32, "Who are you?", "Deny a friend request", new Reward("&a+6000 XP\n&6+6000 Crowns", 600, 0, 600, Collections.emptyMap(), Collections.emptyList()), true, true, AchievementCategory.FRIENDS);
    }
}
