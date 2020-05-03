package network.auroramc.core;

import network.auroramc.core.api.backend.Cache;
import network.auroramc.core.api.command.Command;
import network.auroramc.core.api.permissions.Permission;
import network.auroramc.core.api.permissions.Rank;
import network.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

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
