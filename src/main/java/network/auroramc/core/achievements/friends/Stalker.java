package network.auroramc.core.achievements.friends;

import network.auroramc.core.api.stats.Achievement;

public class Stalker extends Achievement {
    public Stalker() {
        super(34, "Stalker", "Join a server one of your friends is already in", "None", true, true, AchievementCategory.FRIENDS);
    }
}
