package net.auroramc.core.achievements.party;

import net.auroramc.core.api.stats.Achievement;

public class HappyWithMyOwnCompany extends Achievement {
    public HappyWithMyOwnCompany() {
        super(42, "Happy with my own company", "Deny a party request", "None", true, true, AchievementCategory.PARTY);
    }
}
