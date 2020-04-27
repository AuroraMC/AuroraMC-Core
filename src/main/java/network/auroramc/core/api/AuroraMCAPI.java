package network.auroramc.core.api;

import network.auroramc.core.AuroraMC;
import network.auroramc.core.api.backend.Cache;
import network.auroramc.core.api.utils.TextFormatter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class AuroraMCAPI {

    private static AuroraMCAPI i;
    private AuroraMC core;
    private HashMap<Plugin, Cache> caches;
    private TextFormatter formatter;

    public AuroraMCAPI(AuroraMC core) {
        if (i != null) {
            i = this;
            this.core = core;
            caches = new HashMap<>();
            formatter = new TextFormatter();
        }
    }

    @Nullable
    public static Cache getCache(Plugin plugin) {
        return i.caches.get(plugin);
    }

    @Nullable
    public static Cache getCache(String plugin) {
        Plugin javaPlugin = Bukkit.getPluginManager().getPlugin(plugin);
        if (javaPlugin != null) {
            return i.caches.get(javaPlugin);
        }
        return null;
    }

    public static void registerCache(JavaPlugin plugin, Cache cache) {
        i.caches.put(plugin, cache);
    }

    public static TextFormatter getFormatter() {
        return i.formatter;
    }

    public static AuroraMC getCore() {
        return i.core;
    }
}
