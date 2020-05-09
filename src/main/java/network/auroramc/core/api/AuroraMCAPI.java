package network.auroramc.core.api;

import network.auroramc.core.AuroraMC;
import network.auroramc.core.api.backend.Cache;
import network.auroramc.core.api.backend.database.DatabaseManager;
import network.auroramc.core.api.command.Command;
import network.auroramc.core.api.permissions.Permission;
import network.auroramc.core.api.permissions.Rank;
import network.auroramc.core.api.permissions.SubRank;
import network.auroramc.core.api.players.AuroraMCPlayer;
import network.auroramc.core.api.punishments.Punishment;
import network.auroramc.core.api.punishments.Rule;
import network.auroramc.core.api.punishments.RuleBook;
import network.auroramc.core.api.utils.TextFormatter;
import network.auroramc.core.api.utils.gui.GUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AuroraMCAPI {

    private static AuroraMCAPI i;
    private final DatabaseManager dbManager;
    private final AuroraMC core;
    private final HashMap<Plugin, Cache> caches;
    private final TextFormatter formatter;

    //Registering stuff needed by the whole network.
    private final HashMap<Integer, Rank> ranks;
    private final HashMap<Integer, SubRank> subranks;
    private final HashMap<String, Permission> permissions;
    private final HashMap<Player, AuroraMCPlayer> players;
    private final HashMap<String, Command> commands;
    private final HashMap<AuroraMCPlayer, GUI> openGUIs;
    private final RuleBook rules;

    public AuroraMCAPI(AuroraMC core) {
        if (i == null) {
            i = this;
            this.core = core;
            caches = new HashMap<>();
            formatter = new TextFormatter();
            dbManager = new DatabaseManager();

            ranks = new HashMap<>();
            subranks = new HashMap<>();
            permissions = new HashMap<>();
            players = new HashMap<>();
            commands = new HashMap<>();
            openGUIs = new HashMap<>();
            rules = new RuleBook();
        } else {
            throw new UnsupportedOperationException("You cannot initialise the API twice.");
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

    public static DatabaseManager getDbManager() {
        return i.dbManager;
    }

    public static HashMap<Integer, Rank> getRanks() {
        return new HashMap<>(i.ranks);
    }

    public static HashMap<Integer, SubRank> getSubRanks() {
        return new HashMap<>(i.subranks);
    }

    public static HashMap<String, Permission> getPermissions() {
        return new HashMap<>(i.permissions);
    }

    public static void newPlayer(AuroraMCPlayer player) {
        i.players.put(player.getPlayer(), player);
    }

    public static void playerLeave(AuroraMCPlayer player) {
        i.players.remove(player.getPlayer());
    }

    public static AuroraMCPlayer getPlayer(Player player) {
        return i.players.get(player);
    }

    public static ArrayList<AuroraMCPlayer> getPlayers() {
        return new ArrayList<>(i.players.values());
    }

    public static void registerRank(Rank rank) {
        i.ranks.put(rank.getId(), rank);
    }

    public static void registerSubRank(SubRank rank) {
        i.subranks.put(rank.getId(), rank);
    }

    public static void registerPermission(Permission permission) {
        i.permissions.put(permission.getNode(), permission);
    }

    public static void registerCommand(Command command) {
        i.commands.put(command.getMainCommand().toLowerCase(), command);
        for (String alias : command.getAliases()) {
            i.commands.put(alias.toLowerCase(), command);
        }
    }

    public static Command getCommand(String label) {
        return i.commands.get(label);
    }

    public static void openGUI(AuroraMCPlayer player, GUI gui) {
        i.openGUIs.put(player, gui);
    }

    public static GUI getGUI(AuroraMCPlayer player) {
        return i.openGUIs.get(player);
    }

    public static void closeGUI(AuroraMCPlayer player) {
        i.openGUIs.remove(player);
    }

    public static RuleBook getRules() {
        return i.rules;
    }

    public static void loadRules() {
        new BukkitRunnable(){
            @Override
            public void run() {
                i.rules.clear();
                for (Rule rule : i.dbManager.getRules()) {
                    i.rules.registerRule(rule);
                }
            }
        }.runTaskAsynchronously(i.core);
    }
}

