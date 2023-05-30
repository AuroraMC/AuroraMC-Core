/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
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
