/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
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
