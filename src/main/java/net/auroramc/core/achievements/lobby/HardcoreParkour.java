/*
 * Copyright (c) 2021-2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.lobby;

import net.auroramc.core.api.stats.Achievement;

public class HardcoreParkour extends Achievement {
    public HardcoreParkour() {
        super(208, "HARDCORE PARKOUR!", "Spend 5 hours in total in Lobby Parkour courses.", "None", true, false, AchievementCategory.LOBBY);
    }
}
