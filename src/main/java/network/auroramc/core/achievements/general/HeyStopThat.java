package network.auroramc.core.achievements.general;

import network.auroramc.core.api.stats.Achievement;

public class HeyStopThat extends Achievement {
    public HeyStopThat() {
        super(5, "Hey! Stop that!", "Attempt to break a block that is protected.", "None", true, true, AchievementCategory.GENERAL);
    }
}
