package network.auroramc.core.api.stats;

public class AchievementTier {

    private final TieredAcheivement acheivement;
    private final int tier;
    private final long requirement;

    public AchievementTier(TieredAcheivement acheivement, int tier, long requirement) {
        this.acheivement = acheivement;
        this.tier = tier;
        this.requirement = requirement;
    }

    public long getRequirement() {
        return requirement;
    }

    public int getTier() {
        return tier;
    }

    public TieredAcheivement getAcheivement() {
        return acheivement;
    }
}
