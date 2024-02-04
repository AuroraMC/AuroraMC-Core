/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.achievements.general;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class LetsGoShopping extends Achievement {
    public LetsGoShopping() {
        super(13, "Let's Go Shopping", "Follow the link to the store in /help", new Reward("&a+500 XP\n&d+500 Tickets", 500, 500, 0, Collections.emptyMap(), Collections.emptyList()), true, true, AchievementCategory.GENERAL);
    }
}
