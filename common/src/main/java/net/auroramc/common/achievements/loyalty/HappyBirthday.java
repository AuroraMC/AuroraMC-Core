/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.achievements.loyalty;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class HappyBirthday extends Achievement {
    public HappyBirthday() {
        super(50, "Happy Birthday", "Join AuroraMC a year (or more) after you first joined.", new Reward("&a+5000 XP\n&d+5000 Tickets\n&6+5000 Crowns", 5000, 5000, 5000, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.LOYALTY);
    }
}
