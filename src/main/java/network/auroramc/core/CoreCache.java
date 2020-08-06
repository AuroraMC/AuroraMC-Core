package network.auroramc.core;

import network.auroramc.core.api.backend.Cache;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class CoreCache implements Cache {


    private final JavaPlugin owningPlugin;

    public CoreCache(JavaPlugin plugin) {
        owningPlugin = plugin;
    }



    @Override
    public @NotNull JavaPlugin getOwningPlugin() {
        return owningPlugin;
    }
}
