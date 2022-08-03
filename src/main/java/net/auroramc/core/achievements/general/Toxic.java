/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.general;

import net.auroramc.core.api.stats.Achievement;

public class Toxic extends Achievement {
    public Toxic() {
        super(12, "Toxic", "Attempt to say 'ez' or 'trash' after killing someone", "None", true, true, AchievementCategory.GENERAL);
    }
}
