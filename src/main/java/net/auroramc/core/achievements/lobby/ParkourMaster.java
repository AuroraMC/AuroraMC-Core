/*
 * Copyright (c) 2021-2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.lobby;

import net.auroramc.core.api.stats.Achievement;

public class ParkourMaster extends Achievement {
    public ParkourMaster() {
        super(19, "Parkour Master", "Complete all Lobby Parkour Courses", ";&a5000 XP;&d5000 Tickets", true, false, AchievementCategory.LOBBY);
    }
}
