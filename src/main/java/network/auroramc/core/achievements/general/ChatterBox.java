package network.auroramc.core.achievements.general;

import network.auroramc.core.api.stats.AchievementTier;
import network.auroramc.core.api.stats.TieredAcheivement;

public class ChatterBox extends TieredAcheivement {

    public ChatterBox() {
        super(6, "Chatterbox", "Send %s messages in chat", "None", true, false, AchievementCategory.GENERAL);
        this.setTier(1, new AchievementTier(this, 1, 10));
        this.setTier(2, new AchievementTier(this, 2, 100));
        this.setTier(3, new AchievementTier(this, 3, 1000));
        this.setTier(4, new AchievementTier(this, 4, 10000));
        this.setTier(5, new AchievementTier(this, 5, 100000));
        this.setTier(6, new AchievementTier(this, 6, 1000000));
    }
}
