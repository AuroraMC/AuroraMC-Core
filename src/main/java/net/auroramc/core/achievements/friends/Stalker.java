/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.friends;

import net.auroramc.core.api.stats.Achievement;

public class Stalker extends Achievement {
    public Stalker() {
        super(34, "Stalker", "Join a server one of your friends is already in", "&a200 XP;&6200 Crowns", true, true, AchievementCategory.FRIENDS);
    }
}
