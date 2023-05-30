/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.achievements.lobby;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;
import java.util.Collections;

public class TooEasyForMe extends Achievement {
    public TooEasyForMe() {
        super(204, "Too Easy for Me", "Achieve a time of less than 30 seconds on the Easy Parkour!",
                new Reward("&a+1000 XP\n&6+1000 Crowns", 1000, 0, 1000, Collections.emptyMap(), Collections.emptyList()), true, true, AchievementCategory.LOBBY);
    }
}
