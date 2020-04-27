package network.auroramc.core;

import network.auroramc.core.api.AuroraMCAPI;
import org.bukkit.plugin.java.JavaPlugin;

public class AuroraMC extends JavaPlugin {

    private CoreCache cache;
    private static AuroraMC i;

    @Override
    public void onEnable() {
        getLogger().info("Loading AuroraMC-Core...");

        new AuroraMCAPI(this);
        cache = new CoreCache(this);
        AuroraMCAPI.registerCache(this, cache);

        getLogger().info("AuroraMC-Core successfully loaded.");

    }

    public static AuroraMC get() {
        return i;
    }

    @Override
    public void onDisable() {

    }
}
