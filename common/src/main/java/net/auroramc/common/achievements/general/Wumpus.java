/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.achievements.general;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class Wumpus extends Achievement {
    public Wumpus() {
        super(15, "Wumpus", "Link your Discord account with your in-game account", new Reward("&a+5000 XP\n&d+5000 Tickets", 5000, 5000, 0, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.GENERAL);
    }
}
