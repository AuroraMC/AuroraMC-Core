/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.achievements.time;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class ADayOfMyLife extends Achievement {
    public ADayOfMyLife() {
        super(46, "A day of my life...", "Spend 24 hours on AuroraMC", new Reward("&a+10000 XP\n&d+10000 Tickets", 10000, 10000, 0, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.TIME);
    }
}
