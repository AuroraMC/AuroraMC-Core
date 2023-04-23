/*
 * Copyright (c) 2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.achievements.party;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class Late extends Achievement {
    public Late() {
        super(38, "Late", "Join a party that already has 5 people in it", new Reward("&a+1000 XP\n&6+1000 Crowns", 1000, 0, 1000, Collections.emptyMap(), Collections.emptyList()), true, true, AchievementCategory.PARTY);
    }
}
