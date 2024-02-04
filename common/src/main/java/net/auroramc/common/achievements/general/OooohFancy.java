/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.achievements.general;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class OooohFancy extends Achievement {
    public OooohFancy() {
        super(29, "Ooooh Fancy", "Join an in-game AuroraMC Event", new Reward("None", 0, 0, 0, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.GENERAL);
    }
}
