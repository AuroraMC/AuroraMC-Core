/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.game.hotpotato;

import net.auroramc.core.api.stats.Achievement;

public class DamnYouveGotMoves extends Achievement {
    public DamnYouveGotMoves() {
        super(170, "Damn, You've got moves!", "Survive 3 rounds without receiving a Hot Potato from another player.", "None", true, false, AchievementCategory.GAME, 101);
    }
}
