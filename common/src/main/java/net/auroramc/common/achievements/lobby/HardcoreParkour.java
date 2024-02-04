/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.achievements.lobby;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;
import java.util.Collections;

public class HardcoreParkour extends Achievement {
    public HardcoreParkour() {
        super(208, "HARDCORE PARKOUR!", "Spend 5 hours in total in Lobby Parkour courses.",
                new Reward("None", 0, 0, 0, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.LOBBY);
    }

}
