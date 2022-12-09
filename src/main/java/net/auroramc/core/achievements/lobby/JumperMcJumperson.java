/*
 * Copyright (c) 2021-2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.lobby;

import net.auroramc.core.api.stats.AchievementTier;
import net.auroramc.core.api.stats.TieredAcheivement;

public class JumperMcJumperson extends TieredAcheivement {
    public JumperMcJumperson() {
        super(203, "Jumper Mc. Jumperson", "Jump %s times while in a parkour.", "None", true, false, AchievementCategory.LOBBY);
        this.setTier(1, new AchievementTier(this, 1, 100));
        this.setTier(2, new AchievementTier(this, 2, 500));
        this.setTier(3, new AchievementTier(this, 3, 1000));
        this.setTier(4, new AchievementTier(this, 4, 10000));
        this.setTier(5, new AchievementTier(this, 5, 100000));
        this.setTier(6, new AchievementTier(this, 6, 1000000));
    }
}
