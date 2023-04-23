/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.achievements.game.ffa;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class TheUselessUpgrade extends Achievement {


    public TheUselessUpgrade() {
        super(149, "The Useless Upgrade", "Reach FFA Kit Level 100.", new Reward("&a+5000 XP\n&6+5000 Crowns", 5000, 0, 5000, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.GAME);
    }
}
