/*
 * Copyright (c) 2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.achievements.general;
import net.auroramc.api.utils.Reward;

import java.util.Collections;

import net.auroramc.api.stats.AchievementTier;
import net.auroramc.api.stats.TieredAcheivement;

public class ChatterBox extends TieredAcheivement {

    public ChatterBox() {
        super(6, "Chatterbox", "Send %s messages in chat", new Reward("None", 0, 0, 0, Collections.emptyMap(), Collections.emptyList()), true, false, AchievementCategory.GENERAL);
        this.setTier(1, new AchievementTier(this, 1, 10, new Reward("&a+100 XP\n&d+100 Tickets", 100, 100, 0, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(2, new AchievementTier(this, 2, 100, new Reward("&a+250 XP\n&d+250 Tickets", 250, 250, 0, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(3, new AchievementTier(this, 3, 1000, new Reward("&a+500 XP\n&d+500 Tickets", 500, 500, 0, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(4, new AchievementTier(this, 4, 10000, new Reward("&a+1000 XP\n&d+1000 Tickets", 1000, 1000, 0, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(5, new AchievementTier(this, 5, 100000, new Reward("&a+2500 XP\n&d+2500 Tickets", 2500, 2500, 0, Collections.emptyMap(), Collections.emptyList())));
        this.setTier(6, new AchievementTier(this, 6, 1000000, new Reward("&a+5000 XP\n&d+5000 Tickets", 5000, 5000, 0, Collections.emptyMap(), Collections.emptyList())));
    }
}
