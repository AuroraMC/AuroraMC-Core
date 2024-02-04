/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.achievements.party;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class Awkward extends Achievement {
    public Awkward() {
        super(41, "Awkward...", "Leave a party to join a different one.", new Reward("&a+750 XP\n&6+750 Crowns", 750, 0, 750, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.PARTY);
    }
}
