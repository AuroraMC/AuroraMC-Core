/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.achievements.game.hotpotato;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class NopeCantDoThat extends Achievement {


    public NopeCantDoThat() {
        super(166, "Nope, can't do that!", "Try to give the Hot Potato to a player that already has one.", new Reward("&a+500 XP\n&6+500 Crowns", 500, 0, 500, Collections.emptyMap(), Collections.emptyList()), true, true, AchievementCategory.GAME, 101);
    }
}
