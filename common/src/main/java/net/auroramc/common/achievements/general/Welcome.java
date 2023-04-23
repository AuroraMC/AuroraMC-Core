/*
 * Copyright (c) 2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.achievements.general;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class Welcome extends Achievement {
    public Welcome() {
        super(0, "Welcome to AuroraMC", "Join the network for the first time!", new Reward("&a+100 XP\n&6+100 Crowns", 100, 0, 100, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.GENERAL);
    }
}
