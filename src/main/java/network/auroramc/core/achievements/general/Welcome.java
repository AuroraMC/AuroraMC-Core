package network.auroramc.core.achievements.general;

import network.auroramc.core.api.stats.Achievement;

public class Welcome extends Achievement {
    public Welcome() {
        super(0, "Welcome to AuroraMC", "Join the network for the first time!", "None", true, false, AchievementCategory.GENERAL);
    }
}
