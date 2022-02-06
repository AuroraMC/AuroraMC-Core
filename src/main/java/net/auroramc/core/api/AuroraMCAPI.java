/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api;

import net.auroramc.core.AuroraMC;
import net.auroramc.core.api.backend.ServerInfo;
import net.auroramc.core.api.backend.communication.CommunicationUtils;
import net.auroramc.core.api.backend.database.DatabaseManager;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.cosmetics.Cosmetic;
import net.auroramc.core.api.events.player.PlayerLeaveEvent;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.ChatSlowLength;
import net.auroramc.core.api.punishments.Rule;
import net.auroramc.core.api.punishments.RuleBook;
import net.auroramc.core.api.stats.Achievement;
import net.auroramc.core.api.utils.ChatFilter;
import net.auroramc.core.api.utils.TextFormatter;
import net.auroramc.core.api.utils.gui.GUI;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AuroraMCAPI {

    private static DatabaseManager dbManager;
    private static AuroraMC core;
    private static final TextFormatter formatter;

    //Registering stuff needed by the whole network.
    private static final HashMap<Player, AuroraMCPlayer> players;
    private static final HashMap<String, Command> commands;
    private static final HashMap<AuroraMCPlayer, GUI> openGUIs;
    private static final HashMap<Integer, Cosmetic> cosmetics;
    private static final RuleBook rules;
    private static ChatFilter filter;
    private static final HashMap<Integer, Achievement> achievements;

    private static boolean shuttingDown;

    private static ServerInfo serverInfo;

    private static short chatslow;
    private static long chatSilenceEnd;
    private static BukkitTask silenceTask;

    private static boolean rulesLoading;

    static {
        players = new HashMap<>();
        commands = new HashMap<>();
        openGUIs = new HashMap<>();
        rules = new RuleBook();
        achievements = new HashMap<>();
        cosmetics = new HashMap<>();
        formatter = new TextFormatter();

        chatslow = -1;
        chatSilenceEnd = -2;
        silenceTask = null;
        rulesLoading = false;
        shuttingDown = false;
    }


    public static void init(AuroraMC auroraMCCore) {
        if (core == null) {
            core = auroraMCCore;
            dbManager = new DatabaseManager();

            //Identify what server it is on the bungeecord. Grab the details from mysql.
            serverInfo = dbManager.getServerDetailsByName(auroraMCCore.getConfig().getString("name"), auroraMCCore.getConfig().getString("network"));
            if (serverInfo != null) {
                Bukkit.getLogger().info("Server registered as " + serverInfo.getName());
                CommunicationUtils.init();
            } else {
                Bukkit.getLogger().info("I dont know what server I am!");
            }
        } else {
            throw new UnsupportedOperationException("You cannot initialise the API twice.");
        }
    }

    public static TextFormatter getFormatter() {
        return formatter;
    }

    public static AuroraMC getCore() {
        return core;
    }

    public static DatabaseManager getDbManager() {
        return dbManager;
    }

    public static void newPlayer(AuroraMCPlayer player) {
        players.put(player.getPlayer(), player);
    }

    public static void playerLeave(Player player) {
        PlayerLeaveEvent event = new PlayerLeaveEvent(players.get(player));
        Bukkit.getPluginManager().callEvent(event);
        players.remove(player);
    }

    public static AuroraMCPlayer getPlayer(Player player) {
        return players.get(player);
    }

    public static AuroraMCPlayer getDisguisedPlayer(String name) {
        for (AuroraMCPlayer player : players.values()) {
            if (player.getActiveDisguise() != null) {
                if (player.getActiveDisguise().getName().equalsIgnoreCase(name)) {
                    return player;
                }
            }
        }
        return null;
    }

    public static AuroraMCPlayer getPlayer(String name) {
        for (AuroraMCPlayer player : players.values()) {
            if (player.getName().equalsIgnoreCase(name)) {
                return player;
            }
        }
        return null;
    }



    public static AuroraMCPlayer getPlayer(UUID uuid) {
        for (AuroraMCPlayer player : players.values()) {
            if (player.getPlayer().getUniqueId().equals(uuid)) {
                return player;
            }
        }
        return null;
    }

    public static ArrayList<AuroraMCPlayer> getPlayers() {
        return new ArrayList<>(players.values());
    }

    public static void registerAchievement(Achievement achievement) {
        achievements.put(achievement.getAchievementId(), achievement);
    }

    public static void registerCommand(Command command) {
        commands.put(command.getMainCommand().toLowerCase(), command);
        for (String alias : command.getAliases()) {
            commands.put(alias.toLowerCase(), command);
        }
    }

    public static Command getCommand(String label) {
        return commands.get(label);
    }

    public static Achievement getAchievement(int id) {
        return achievements.get(id);
    }

    public static Achievement getAchievement(String name) {
        for (Achievement achievement : achievements.values()) {
            if (achievement.getName().equalsIgnoreCase(name)) {
                return achievement;
            }
        }
        return null;
    }

    public static List<String> getCommands() {
        return new ArrayList<>(commands.keySet());
    }

    public static void openGUI(AuroraMCPlayer player, GUI gui) {
        openGUIs.put(player, gui);
    }

    public static GUI getGUI(AuroraMCPlayer player) {
        return openGUIs.get(player);
    }

    public static void closeGUI(AuroraMCPlayer player) {
        openGUIs.remove(player);
    }

    public static void registerCosmetic(Cosmetic cosmetic) {
        cosmetics.put(cosmetic.getId(), cosmetic);
    }

    public static HashMap<Integer, Cosmetic> getCosmetics() {
        return new HashMap<>(cosmetics);
    }

    public static RuleBook getRules() {
        return rules;
    }

    public static void loadRules() {
        rulesLoading = true;
        new BukkitRunnable(){
            @Override
            public void run() {
                rules.clear();
                for (Rule rule : dbManager.getRules()) {
                    rules.registerRule(rule);
                }
                rulesLoading = false;
            }
        }.runTaskAsynchronously(core);
        loadFilter();
    }

    public static void loadFilter() {
        filter = null;
        new BukkitRunnable(){
            @Override
            public void run() {
                filter = dbManager.loadFilter();
            }
        }.runTaskAsynchronously(core);
    }

    public static ChatFilter getFilter() {
        return filter;
    }

    public static ServerInfo getServerInfo() {
        return serverInfo;
    }


    public static int getOpenGUIs() {
        return openGUIs.size();
    }

    public static HashMap<Integer, Achievement> getAchievements() {
        return achievements;
    }

    public static short getChatSlow() {return chatslow;}

    public static void setChatSlow(short chatSlow) {
        chatslow = chatSlow;
    }

    public static long getChatSilenceEnd() {
        return chatSilenceEnd;
    }

    public static BukkitTask getSilenceTask() {
        return silenceTask;
    }

    public static void enableChatSilence(short seconds, boolean sendMessage) {
        if (silenceTask != null) {
            silenceTask.cancel();
        }
        if (seconds != -1) {
            chatSilenceEnd = System.currentTimeMillis() + (seconds*1000);
            silenceTask = new BukkitRunnable(){
                @Override
                public void run() {
                    if (chatSilenceEnd != -2) {
                        disableSilence();
                    }
                }
            }.runTaskLater(core, seconds*20);
        } else {
            chatSilenceEnd = -1;
        }

        ChatSlowLength length = new ChatSlowLength(seconds);
        if (sendMessage) {
            for (AuroraMCPlayer player : getPlayers()) {
                if (player.hasPermission("moderation") || player.hasPermission("social") ||  player.hasPermission("debug.info")) {
                    player.getPlayer().sendMessage(formatter.pluginMessage("Silence", String.format("The chat has been silenced for **%s**. The goose has granted you immunity from it because of your rank!", length.getFormatted())));
                } else {
                    player.getPlayer().sendMessage(formatter.pluginMessage("Silence", String.format("The chat has been silenced for **%s**.", length.getFormatted())));
                }
            }
        }
    }

    public static void disableSilence() {
        chatSilenceEnd = -2;
        for (AuroraMCPlayer player : getPlayers()) {
            player.getPlayer().sendMessage(formatter.pluginMessage("Silence", "Chat is no longer silenced."));
        }
        if (silenceTask != null) {
            BukkitTask task = silenceTask;
            silenceTask = null;
            task.cancel();
        }
    }

    public static boolean isRulesLoading() {
        return rulesLoading;
    }

    public static boolean isShuttingDown() {
        return shuttingDown;
    }

    public static void setShuttingDown(boolean shuttingDown) {
        AuroraMCAPI.shuttingDown = shuttingDown;
    }
}

