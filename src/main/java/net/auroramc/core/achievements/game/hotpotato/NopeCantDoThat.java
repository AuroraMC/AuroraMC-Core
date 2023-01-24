/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.achievements.game.hotpotato;

import net.auroramc.core.api.stats.Achievement;

public class NopeCantDoThat extends Achievement {
    public NopeCantDoThat() {
        super(166, "Nope, can't do that!", "Try to give the Hot Potato to a player who already has one.", ";&a500 XP;&6500 Crowns", true, true, AchievementCategory.GAME, 101);
    }
}
