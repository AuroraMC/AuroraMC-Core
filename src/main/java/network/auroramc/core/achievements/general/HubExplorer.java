package network.auroramc.core.achievements.general;

import network.auroramc.core.api.stats.AchievementTier;
import network.auroramc.core.api.stats.TieredAcheivement;

public class HubExplorer extends TieredAcheivement {
    public HubExplorer () {
        super(11, "Hub Explorer", "Spend %s hours in hub", "None", true, true, AchievementCategory.GENERAL);
        this.setTier(1, new AchievementTier(this, 1, 24));
        this.setTier(2, new AchievementTier(this, 2, 48));
        this.setTier(3, new AchievementTier(this, 3, 168));
        this.setTier(4, new AchievementTier(this, 4, 504));
        this.setTier(5, new AchievementTier(this, 5, 744));
        this.setTier(6, new AchievementTier(this, 6, 1440));
    }
}
