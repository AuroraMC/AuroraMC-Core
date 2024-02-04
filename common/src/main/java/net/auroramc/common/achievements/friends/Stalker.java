/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.achievements.friends;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class Stalker extends Achievement {
    public Stalker() {
        super(34, "Stalker", "Join a server one of your friends is already in", new Reward("&a+200 XP\n&6+200 Crowns", 200, 0, 200, Collections.emptyMap(), Collections.emptyList()), true, true, AchievementCategory.FRIENDS);
    }
}
