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

    private final HashMap<Integer, Rank> ranks;
    private final HashMap<String, Permission> permissions;
    private final HashMap<Player, AuroraMCPlayer> players;
    private final HashMap<String, Command> commands;

    private JavaPlugin owningPlugin;

    public CoreCache(JavaPlugin plugin) {
        owningPlugin = plugin;
        ranks = new HashMap<>();
        permissions = new HashMap<>();
        players = new HashMap<>();
        commands = new HashMap<>();
    }

    public HashMap<Integer, Rank> getRanks() {
        return new HashMap<>(ranks);
    }

    public HashMap<String, Permission> getPermissions() {
        return new HashMap<>(permissions);
    }

    public void newPlayer(AuroraMCPlayer player) {
        players.put(player.getPlayer(), player);
    }

    public void playerLeave(AuroraMCPlayer player) {
        players.remove(player.getPlayer());
    }

    public AuroraMCPlayer getPlayer(Player player) {
        return players.get(player);
    }

    public void registerRank(Rank rank) {
        ranks.put(rank.getId(), rank);
    }

    public void registerPermission(Permission permission) {
        permissions.put(permission.getNode(), permission);
    }

    public void registerCommand(Command command) {
        commands.put(command.getMainCommand().toLowerCase(), command);
        for (String alias : command.getAliases()) {
            commands.put(alias.toLowerCase(), command);
        }
    }

    public Command getCommand(String label) {
        return commands.get(label);
    }

    @Override
    public @NotNull JavaPlugin getOwningPlugin() {
        return owningPlugin;
    }
}
