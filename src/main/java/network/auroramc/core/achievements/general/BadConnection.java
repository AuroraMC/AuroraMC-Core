package network.auroramc.core.achievements.general;

import network.auroramc.core.api.stats.Achievement;

public class BadConnection extends Achievement {
    public BadConnection() {
        super(23, "Bad Connection", "Leave and rejoin 5 times in 15 minutes", "None", true, false, AchievementCategory.GENERAL);
    }
}