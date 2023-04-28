/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.achievements.lobby;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;
import java.util.Collections;

public class WhoopsMyBad extends Achievement {
    public WhoopsMyBad() {
        super(207, "Whoops my bad", "While in a parkour, run over a pressure plate of a different parkour.",
                new Reward("None", 0, 0, 0, Collections.emptyMap(), Collections.emptyList()), true, true, AchievementCategory.LOBBY);
    }
}
