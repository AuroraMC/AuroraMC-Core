package net.auroramc.core.api;

import net.auroramc.core.AuroraMC;
import net.auroramc.core.api.backend.ServerInfo;
import net.auroramc.core.api.backend.database.DatabaseManager;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.permissions.SubRank;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.ChatSlowLength;
import net.auroramc.core.api.punishments.Rule;
import net.auroramc.core.api.punishments.RuleBook;
import net.auroramc.core.api.stats.Achievement;
import net.auroramc.core.api.utils.ChatFilter;
import net.auroramc.core.api.utils.TextFormatter;
import net.auroramc.core.api.utils.gui.GUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AuroraMCAPI {

    private static AuroraMCAPI i;
    private final DatabaseManager dbManager;
    private final AuroraMC core;
    private final TextFormatter formatter;

    //Registering stuff needed by the whole network.
    private final HashMap<Integer, Rank> ranks;
    private final HashMap<Integer, SubRank> subranks;
    private final HashMap<String, Permission> permissions;
    private final HashMap<Player, AuroraMCPlayer> players;
    private final HashMap<String, Command> commands;
    private final HashMap<AuroraMCPlayer, GUI> openGUIs;
    private final RuleBook rules;
    private ChatFilter filter;
    private final HashMap<Integer, Achievement> achievements;

    private final HashMap<Player, String> pendingDisguiseChecks;

    private final ServerInfo serverInfo;

    private short chatslow;
    private long chatSilenceEnd;
    private BukkitTask silenceTask;

    public AuroraMCAPI(AuroraMC core) {
        if (i == null) {
            i = this;
            this.core = core;
            formatter = new TextFormatter();
            dbManager = new DatabaseManager();

            ranks = new HashMap<>();
            subranks = new HashMap<>();
            permissions = new HashMap<>();
            players = new HashMap<>();
            commands = new HashMap<>();
            openGUIs = new HashMap<>();
            rules = new RuleBook();
            pendingDisguiseChecks = new HashMap<>();
            achievements = new HashMap<>();

            chatslow = -1;
            chatSilenceEnd = -2;
            silenceTask = null;

            //Identify what server it is on the bungeecord. Grab the details from mysql.

            serverInfo = dbManager.getServerDetails(Bukkit.getIp(), Bukkit.getPort());
            Bukkit.getLogger().info("Server registered as " + serverInfo.getName());
        } else {
            throw new UnsupportedOperationException("You cannot initialise the API twice.");
        }
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

    public static void playerLeave(Player player) {
        i.players.remove(player);
    }

    public static AuroraMCPlayer getPlayer(Player player) {
        return i.players.get(player);
    }

    public static AuroraMCPlayer getPlayer(String name) {
        for (AuroraMCPlayer player : i.players.values()) {
            if (player.getName().equalsIgnoreCase(name)) {
                return player;
            }
        }
        return null;
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

    public static void registerAchievement(Achievement achievement) {
        i.achievements.put(achievement.getAchievementId(), achievement);
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

    public static Achievement getAchievement(int id) {
        return i.achievements.get(id);
    }

    public static Achievement getAchievement(String name) {
        for (Achievement achievement : i.achievements.values()) {
            if (achievement.getName().equalsIgnoreCase(name)) {
                return achievement;
            }
        }
        return null;
    }

    public static List<String> getCommands() {
        return new ArrayList<>(i.commands.keySet());
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

    public static void loadFilter() {
        new BukkitRunnable(){
            @Override
            public void run() {
                i.filter = i.dbManager.loadFilter();
            }
        }.runTaskAsynchronously(i.core);
    }

    public static ChatFilter getFilter() {
        return i.filter;
    }

    public static ServerInfo getServerInfo() {
        return i.serverInfo;
    }

    public static HashMap<Player, String> getPendingDisguiseChecks() {
        return i.pendingDisguiseChecks;
    }

    public static int getOpenGUIs() {
        return i.openGUIs.size();
    }

    public static HashMap<Integer, Achievement> getAchievements() {
        return i.achievements;
    }

    public static short getChatSlow() {return i.chatslow;}

    public static void setChatSlow(short chatSlow) {
        i.chatslow = chatSlow;
    }

    public static long getChatSilenceEnd() {
        return i.chatSilenceEnd;
    }

    public static BukkitTask getSilenceTask() {
        return i.silenceTask;
    }

    public static void enableChatSilence(short seconds, boolean sendMessage) {
        if (i.silenceTask != null) {
            i.silenceTask.cancel();
        }
        if (seconds != -1) {
            i.chatSilenceEnd = System.currentTimeMillis() + (seconds*1000);
            i.silenceTask = new BukkitRunnable(){
                @Override
                public void run() {
                    if (i.chatSilenceEnd != -2) {
                        disableSilence();
                    }
                }
            }.runTaskLater(i.core, seconds*20);
        } else {
            i.chatSilenceEnd = -1;
        }

        ChatSlowLength length = new ChatSlowLength(seconds);
        if (sendMessage) {
            for (AuroraMCPlayer player : getPlayers()) {
                if (player.hasPermission("moderation") || player.hasPermission("social") ||  player.hasPermission("debug.info")) {
                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Silence", String.format("The chat has been silenced for **%s**. The goose has granted you immunity from it because of your rank!", length.getFormatted())));
                } else {
                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Silence", String.format("The chat has been silenced for **%s**.", length.getFormatted())));
                }
            }
        }
    }

    public static void disableSilence() {
        i.chatSilenceEnd = -2;
        for (AuroraMCPlayer player : getPlayers()) {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Silence", "Chat is no longer silenced."));
        }
        if (i.silenceTask != null) {
            BukkitTask task = i.silenceTask;
            i.silenceTask = null;
            task.cancel();
        }
    }
}

