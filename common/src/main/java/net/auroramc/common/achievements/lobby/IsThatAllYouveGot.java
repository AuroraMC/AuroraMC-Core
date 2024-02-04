/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.achievements.lobby;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.Reward;
import java.util.Collections;

public class IsThatAllYouveGot extends Achievement {
    public IsThatAllYouveGot() {
        super(206, "Is that all you've got?", "Achieve a time of less than 5 minutes 30 seconds on the Hard Parkour!",
                new Reward("&a+5000 XP\n&d+5000 Tickets\n&6+5000 Crowns", 5000, 5000, 5000, Collections.emptyMap(), Collections.emptyList()), true, true, AchievementCategory.LOBBY);
    }

}
