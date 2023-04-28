/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.achievements.lobby;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;
import java.util.Collections;

public class DamnThatWasChallenging extends Achievement {

    public DamnThatWasChallenging() {
        super(205, "Damn, that was challenging!", "Achieve a time of less than 2 minutes 30 seconds on the Medium Parkour!", new Reward("&a+5000 XP\n&6+5000 Crowns", 5000, 0, 5000, Collections.emptyMap(), Collections.emptyList()), true, true, AchievementCategory.LOBBY);
    }
}
