/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.achievements.general;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class AmIFamousYet extends Achievement {
    public AmIFamousYet() {
        super(17, "Am I famous yet?", "Be in the same Lobby as a YouTuber or Twitch for the first time.", new Reward("&a+2000 XP\n&d+2000 Tickets", 2000, 2000, 0, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.GENERAL);
    }
}
