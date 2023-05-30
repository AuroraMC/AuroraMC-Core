/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.achievements.party;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class YoureComingWithMe extends Achievement {
    public YoureComingWithMe() {
        super(37, "You're coming with me", "Move everyone in your party to a new server", new Reward("&a+100 XP\n&6+100 Crowns", 100, 0, 100, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.PARTY);
    }
}
