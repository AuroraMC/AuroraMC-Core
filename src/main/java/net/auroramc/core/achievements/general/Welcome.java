package net.auroramc.core.achievements.general;

import net.auroramc.core.api.stats.Achievement;

public class Welcome extends Achievement {
    public Welcome() {
        super(0, "Welcome to AuroraMC", "Join the network for the first time!", "None", true, false, AchievementCategory.GENERAL);
    }
}
