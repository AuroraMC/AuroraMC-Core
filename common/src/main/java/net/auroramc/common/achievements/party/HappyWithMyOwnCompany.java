/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.achievements.party;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class HappyWithMyOwnCompany extends Achievement {
    public HappyWithMyOwnCompany() {
        super(42, "Happy with my own company", "Deny a party request", new Reward("&a+500 XP\n&6+500 Crowns", 500, 0, 500, Collections.emptyMap(), Collections.emptyList()), true, true, AchievementCategory.PARTY);
    }
}
