/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.game.hotpotato;

import net.auroramc.core.api.stats.Achievement;

public class DamnThatWasClose extends Achievement {
    public DamnThatWasClose() {
        super(161, "Damn, that was close...", "Pass the potato to another player less than 3 seconds before it explodes.", ";&a1000 XP;&61000 Crowns", true, true, AchievementCategory.GAME, 101);
    }
}
