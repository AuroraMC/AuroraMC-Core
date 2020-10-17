package net.auroramc.core.api.backend;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public interface Cache {

    @NotNull
    JavaPlugin getOwningPlugin();
}
