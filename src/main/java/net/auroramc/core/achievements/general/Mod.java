/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.general;

import net.auroramc.core.api.stats.Achievement;

public class Mod extends Achievement {
    public Mod() {
        super(24, "Mod?", "Be in the same lobby as a member of Moderation Staff", ";&a1000 XP;&d1000 Tickets", true, false, AchievementCategory.GENERAL);
    }
}
