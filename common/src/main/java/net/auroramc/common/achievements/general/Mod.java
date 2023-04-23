/*
 * Copyright (c) 2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.achievements.general;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class Mod extends Achievement {
    public Mod() {
        super(24, "Mod?", "Be in the same lobby as a member of Moderation Staff", new Reward("&a+1000 XP\n&d+1000 Tickets", 1000, 1000, 0, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.GENERAL);
    }
}
