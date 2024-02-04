/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.achievements.time;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class ThatsFarTooLong extends Achievement {
    public ThatsFarTooLong() {
        super(49, "That's far too long", "Spend two weeks on AuroraMC", new Reward("&a+100000 XP\n&d+100000 Tickets", 100000, 100000, 0, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.TIME);
    }
}
