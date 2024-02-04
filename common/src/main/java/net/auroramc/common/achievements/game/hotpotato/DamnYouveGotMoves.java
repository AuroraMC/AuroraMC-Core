/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.achievements.game.hotpotato;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class DamnYouveGotMoves extends Achievement {


    public DamnYouveGotMoves() {
        super(170, "Damn, you've got moves!", "Survive 3 rounds without receiving a Hot Potato from another player.", new Reward("&a+1000 XP\n&6+1000 Crowns", 1000, 0, 1000, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.GAME, 101);
    }
}
