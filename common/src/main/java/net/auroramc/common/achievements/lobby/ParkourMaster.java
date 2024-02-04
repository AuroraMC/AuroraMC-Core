/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.achievements.lobby;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class ParkourMaster extends Achievement {
    public ParkourMaster() {
        super(19, "Parkour Master", "Complete all Hub Parkour Courses", new Reward("&a+5000 XP\n&d+5000 Tickets", 5000, 5000, 0, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.LOBBY);
    }
}
