/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.api;

import net.auroramc.api.abstraction.AbstractedMethods;
import net.auroramc.api.abstraction.ScheduleFactory;
import net.auroramc.api.backend.bigbrother.WDLoggerHandler;
import net.auroramc.api.backend.bigbrother.Watchdog;
import net.auroramc.api.backend.bigbrother.WDUncaughtExceptionHandler;
import net.auroramc.api.backend.info.*;
import net.auroramc.api.backend.database.DatabaseManager;
import net.auroramc.api.command.Command;
import net.auroramc.api.cosmetics.Cosmetic;
import net.auroramc.api.cosmetics.CosmeticExecutor;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.api.punishments.Rule;
import net.auroramc.api.punishments.RuleBook;
import net.auroramc.api.stats.Achievement;
import net.auroramc.api.utils.ChatFilter;
import net.auroramc.api.utils.Reward;
import net.auroramc.common.CommonUtils;

import java.util.*;
import java.util.logging.Logger;

public class AuroraMCAPI {

    private static final long startTime;

    private static AbstractedMethods abstractedMethods;

    private static boolean cosmeticsEnabled;
    private static boolean testServer;

    private static DatabaseManager dbManager;
    private static Logger logger;

    //Registering stuff needed by the whole network.
    private static final HashMap<String, Command> commands;
    private static final HashMap<Integer, Cosmetic> cosmetics;
    private static final HashMap<Integer, CosmeticExecutor> cosmeticExecutors;
    private static final RuleBook rules;
    private static ChatFilter filter;
    private static final HashMap<Integer, Achievement> achievements;
    private static final HashMap<Integer, Reward> levelRewards;

    private static boolean shuttingDown;

    private static Info info;

    private static short chatslow;
    private static long chatSilenceEnd;

    private static boolean rulesLoading;

    private static Watchdog watchdog;

    static {
        commands = new HashMap<>();
        rules = new RuleBook();
        achievements = new HashMap<>();
        cosmetics = new HashMap<>();
        cosmeticExecutors = new HashMap<>();
        levelRewards = new HashMap<>();

        chatslow = -1;
        chatSilenceEnd = -2;
        rulesLoading = false;
        shuttingDown = false;

        cosmeticsEnabled = true;

        startTime = System.currentTimeMillis();
    }


    public static void init(Logger logger, AbstractedMethods methods, String host, String port, String db, String username, String password, String name, String network, String redisHost, String redisAuth, boolean proxy) {
        AuroraMCAPI.logger = logger;
        Thread.setDefaultUncaughtExceptionHandler(new WDUncaughtExceptionHandler());
        logger.addHandler(new WDLoggerHandler());
        abstractedMethods = methods;
        dbManager = new DatabaseManager(host, port, db, username, password, redisHost, redisAuth);


        //Identify what server it is on the bungeecord. Grab the details from mysql.
        if (proxy) {
            info = dbManager.getProxyInfo(name, network);
        } else {
            info = dbManager.getServerDetailsByName(name, network);
        }

        if (info != null) {
            logger.info("Registered as " + info.getName());
            testServer = info.getNetwork() == Info.Network.TEST;
        } else {
            logger.info("I dont know what server I am!");
            testServer = false;
        }

        CommonUtils.loadAchievements();
        CommonUtils.loadCosmetics();
        CommonUtils.loadRewards();
    }

    public static DatabaseManager getDbManager() {
        return dbManager;
    }

    public static void registerAchievement(Achievement achievement) {
        achievements.put(achievement.getAchievementId(), achievement);
    }

    public static void registerCommand(Command<? extends AuroraMCPlayer> command) {
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

    public static void registerCosmetic(Cosmetic cosmetic) {
        cosmetics.put(cosmetic.getId(), cosmetic);
    }

    public static void registerCosmeticExecutor(CosmeticExecutor cosmetic) {
        cosmeticExecutors.put(cosmetic.getCosmetic().getId(), cosmetic);
    }

    public static HashMap<Integer, CosmeticExecutor> getCosmeticExecutors() {
        return cosmeticExecutors;
    }

    public static HashMap<Integer, Cosmetic> getCosmetics() {
        return new HashMap<>(cosmetics);
    }

    public static RuleBook getRules() {
        return rules;
    }

    public static void loadRules() {
        rulesLoading = true;
        ScheduleFactory.scheduleAsync(() -> {
            rules.clear();
            for (Rule rule : dbManager.getRules()) {
                rules.registerRule(rule);
            }
            rulesLoading = false;
        });
        loadFilter();
    }

    public static void loadFilter() {
        filter = null;
        ScheduleFactory.scheduleAsync(() -> filter = dbManager.loadFilter());
    }

    public static ChatFilter getFilter() {
        return filter;
    }

    public static Info getInfo() {
        return info;
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

    public static void enableChatSilence(short seconds) {
        if (seconds != -1) {
            chatSilenceEnd = System.currentTimeMillis() + (seconds*1000);
        } else {
            chatSilenceEnd = -1;
        }
    }

    public static void disableSilence() {
        chatSilenceEnd = -2;
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

    public static boolean isCosmeticsEnabled() {
        return cosmeticsEnabled;
    }

    public static boolean isTestServer() {
        return testServer;
    }

    public static void setCosmeticsEnabled(boolean cosmeticsEnabled) {
        AuroraMCAPI.cosmeticsEnabled = cosmeticsEnabled;
    }

    public static void setTestServer(boolean testServer) {
        AuroraMCAPI.testServer = testServer;
    }


    public static long getStartTime() {
        return startTime;
    }

    public static AbstractedMethods getAbstractedMethods() {
        return abstractedMethods;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static HashMap<Integer, Reward> getLevelRewards() {
        return levelRewards;
    }


}

