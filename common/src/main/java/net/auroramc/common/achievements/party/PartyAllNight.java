/*
 * Copyright (c) 2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.achievements.party;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class PartyAllNight extends Achievement {
    public PartyAllNight() {
        super(40, "Party all night!", "Be in the same party for 3 hours", new Reward("&a+750 XP\n&6+750 Crowns", 750, 0, 750, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.PARTY);
    }
}
