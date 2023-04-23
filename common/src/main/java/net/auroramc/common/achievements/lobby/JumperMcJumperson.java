/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.achievements.lobby;

import net.auroramc.api.stats.AchievementTier;
import net.auroramc.api.utils.Reward;
import net.auroramc.api.stats.TieredAcheivement;
import java.util.Collections;

public class JumperMcJumperson extends TieredAcheivement {

    public JumperMcJumperson() {
        super(203, "Jumper Mc. Jumperson", "Jump %s times while in a parkour.",
                new Reward("None", 0, 0, 0, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.LOBBY);
        this.setTier(1, new AchievementTier(this, 1, 100, new Reward("None", 0, 0, 0, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(2, new AchievementTier(this, 2, 500, new Reward("None", 0, 0, 0, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(3, new AchievementTier(this, 3, 1000, new Reward("None", 0, 0, 0, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(4, new AchievementTier(this, 4, 10000, new Reward("None", 0, 0, 0, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(5, new AchievementTier(this, 5, 100000, new Reward("None", 0, 0, 0, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(6, new AchievementTier(this, 6, 1000000, new Reward("None", 0, 0, 0, Collections.emptyMap(), Collections.emptyList())));
    }
}

