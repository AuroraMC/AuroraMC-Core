package network.auroramc.core.achievements.party;

import network.auroramc.core.api.stats.Achievement;

public class Late extends Achievement {
    public Late() {
        super(38, "Late", "Join a party that already has 5 people in it", "None", true, true, AchievementCategory.PARTY);
    }
}
