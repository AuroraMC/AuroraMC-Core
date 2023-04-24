/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.achievements.game.crystalquest;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

public class HeyStopThatsYourFriend extends Achievement {


    public HeyStopThatsYourFriend() {
        super(67, "Hey, Stop! That's your friend!", "Attempt to kill one of your mining robots.", new Reward("&a+500 XP\n&6+500 Crowns", 500, 0, 500, Collections.emptyMap(), Collections.emptyList()), true, true, AchievementCategory.GAME, 1);
    }
}
