/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.game.hotpotato;

import net.auroramc.core.api.stats.Achievement;

public class OuchThatsHot extends Achievement {
    public OuchThatsHot() {
        super(164, "Ouch, that's hot!", "Die from receiving a Hot Potato in the last 3 seconds of the round", "None", true, true, AchievementCategory.GAME, 101);
    }
}
