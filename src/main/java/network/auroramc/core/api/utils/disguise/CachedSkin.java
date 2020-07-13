package network.auroramc.core.api.utils.disguise;

public class CachedSkin extends Skin {

    private final long lastUpdated;

    public CachedSkin(String value, String signature, long lastUpdated) {
        super(value, signature);
        this.lastUpdated = lastUpdated;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }
}
