/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.achievements.game.spleef;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class WaitIsThisPrisons extends Achievement {

    public WaitIsThisPrisons() {
        super(127, "Wait, is this Prisons?", "Break 300 blocks in one game", new Reward("&a+1000 XP\n&6+1000 Crowns", 1000, 0, 1000, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.GAME, 100);
    }
}
