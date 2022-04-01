/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.stats;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.LevelUtils;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class PlayerStatistics {

    private final AuroraMCPlayer player;
    private final long firstJoinTimestamp;
    private long totalXpEarned;
    private long xpIntoLevel;
    private int level;
    private final Map<Achievement, Integer> achievementsGained;
    private final Map<Achievement, Long> achievementProgress;
    private final Map<Integer, GameStatistics> stats;
    private long lobbyTimeMs;
    private long gameTimeMs;
    private long gamesPlayed;
    private long gamesWon;
    private long gamesLost;
    private long ticketsEarned;
    private long crownsEarned;


    public PlayerStatistics(AuroraMCPlayer player, long firstJoinTimestamp, long totalXpEarned, long xpIntoLevel, int level, Map<Achievement, Integer> achievementsGained, Map<Achievement, Long> achievementProgress, Map<Integer, GameStatistics> stats, long lobbyTimeMs, long gameTimeMs, long gamesPlayed, long gamesWon, long gamesLost, long ticketsEarned, long crownsEarned) {
        this.firstJoinTimestamp = firstJoinTimestamp;
        this.player = player;
        this.totalXpEarned = totalXpEarned;
        this.xpIntoLevel = xpIntoLevel;
        this.level = level;
        this.achievementsGained = achievementsGained;
        this.achievementProgress = achievementProgress;
        this.stats = stats;
        this.lobbyTimeMs = lobbyTimeMs;
        this.gameTimeMs = gameTimeMs;
        this.gamesWon = gamesWon;
        this.gamesPlayed = gamesPlayed;
        this.gamesLost = gamesLost;
        this.ticketsEarned = ticketsEarned;
        this.crownsEarned = crownsEarned;
    }

    public long getGameTimeMs() {
        return gameTimeMs;
    }

    public long getLevel() {
        return level;
    }

    public long getLobbyTimeMs() {
        return lobbyTimeMs;
    }

    public long getTotalXpEarned() {
        return totalXpEarned;
    }

    public long getXpIntoLevel() {
        return xpIntoLevel;
    }

    public Map<Achievement, Integer> getAchievementsGained() {
        return new HashMap<>(achievementsGained);
    }

    public Map<Integer, GameStatistics> getStats() {
        return new HashMap<>(stats);
    }

    public AuroraMCPlayer getPlayer() {
        return player;
    }

    public void addLevels(int amount) {
        level += amount;
        xpIntoLevel = 0;
    }

    public void removeLevels(int amount) {
        level -= amount;
        xpIntoLevel = 0;
    }

    public boolean addXp(long amount, boolean sendToBungee) {
        xpIntoLevel += amount;
        totalXpEarned += amount;
        boolean levelUp = false;

        if (LevelUtils.xpForLevel(level + 1) != -1) {
            if (xpIntoLevel >= LevelUtils.xpForLevel(level + 1)) {
                do {
                    level++;
                    if (player != null) {
                        switch (level) {
                            case 200: {
                                if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(60))) {
                                    player.getStats().achievementGained(AuroraMCAPI.getAchievement(60), 1, true);
                                }
                            }
                            case 150: {
                                if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(59))) {
                                    player.getStats().achievementGained(AuroraMCAPI.getAchievement(59), 1, true);
                                }
                            }
                            case 125: {
                                if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(58))) {
                                    player.getStats().achievementGained(AuroraMCAPI.getAchievement(58), 1, true);
                                }
                            }
                            case 100: {
                                if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(57))) {
                                    player.getStats().achievementGained(AuroraMCAPI.getAchievement(57), 1, true);
                                }
                            }
                            case 80: {
                                if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(56))) {
                                    player.getStats().achievementGained(AuroraMCAPI.getAchievement(56), 1, true);
                                }
                            }
                            case 60: {
                                if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(55))) {
                                    player.getStats().achievementGained(AuroraMCAPI.getAchievement(55), 1, true);
                                }
                            }
                            case 40: {
                                if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(54))) {
                                    player.getStats().achievementGained(AuroraMCAPI.getAchievement(54), 1, true);
                                }
                            }
                            case 25: {
                                if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(53))) {
                                    player.getStats().achievementGained(AuroraMCAPI.getAchievement(53), 1, true);
                                }
                            }
                            case 10: {
                                if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(52))) {
                                    player.getStats().achievementGained(AuroraMCAPI.getAchievement(52), 1, true);
                                }
                            }
                            case 1: {
                                if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(51))) {
                                    player.getStats().achievementGained(AuroraMCAPI.getAchievement(51), 1, true);
                                }
                            }
                        }
                    }
                    xpIntoLevel = (xpIntoLevel - LevelUtils.xpForLevel(level));
                } while (xpIntoLevel >= LevelUtils.xpForLevel(level + 1) && LevelUtils.xpForLevel(level + 1) != -1);
                levelUp = true;
            }
        }

        if (sendToBungee) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("XPAdd");
            out.writeUTF(player.getName());
            out.writeLong(amount);
            player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
        }
        return levelUp;
    }

    public void removeXp(long amount) {
        totalXpEarned -= amount;
        if (amount > xpIntoLevel) {
            do {
                level--;
                amount -= xpIntoLevel;
                xpIntoLevel = LevelUtils.xpForLevel(level + 1);
            } while (amount > xpIntoLevel && LevelUtils.xpForLevel(level) != -1);
        }
        xpIntoLevel -= amount;
    }

    public void incrementStatistic(int gameId, String key, long amount, boolean sendToBungee) {
        if (stats.containsKey(gameId)) {
            stats.get(gameId).addStat(key, amount);
        } else {
            Map<String, Long> map = new HashMap<>();
            map.put(key, amount);
            stats.put(gameId, new GameStatistics(gameId, map));
        }

        if (sendToBungee) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("StatisticIncrement");
            out.writeUTF(player.getName());
            out.writeInt(gameId);
            out.writeUTF(key);
            out.writeLong(amount);

            player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
        }
    }

    public long getStatistic(int gameId, String key) {
        if (stats.get(gameId) == null) {
            return 0;
        }

        return stats.get(gameId).getStat(key);
    }

    public GameStatistics getGameStatistics(int gameId) {
        return stats.get(gameId);
    }

    public void achievementGained(Achievement achievement, int tier, boolean sendToBungee) {
        achievementsGained.put(achievement, tier);

        if (sendToBungee) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("AchievementGained");
            out.writeUTF(player.getName());
            out.writeInt(achievement.getAchievementId());
            out.writeInt(tier);
            player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
        }
    }

    public void achievementRemoved(Achievement achievement) {
        achievementsGained.remove(achievement);
    }

    public Map<Achievement, Long> getAchievementProgress() {
        return new HashMap<>(achievementProgress);
    }

    public void setAchievementProgress(Achievement achievement, long amount) {
        if (amount == 0) {
            achievementProgress.remove(achievement);
        } else {
            achievementProgress.put(achievement, amount);
        }
    }

    public int addProgress(Achievement achievement, long amount, int currentTier, boolean sendToBungee) {
        if (achievement instanceof TieredAcheivement) {
            TieredAcheivement tieredAcheivement = (TieredAcheivement) achievement;
            if (achievementProgress.containsKey(achievement)) {
                if (tieredAcheivement.achievedTier(currentTier, amount + achievementProgress.get(achievement)) != -1) {
                    int tierAchieved = currentTier + 1;
                    while (tieredAcheivement.achievedTier(tierAchieved, amount + achievementProgress.get(achievement)) != -1) {
                        tierAchieved++;
                    }
                    achievementProgress.put(achievement, achievementProgress.get(achievement) + amount);

                    if (sendToBungee) {
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("AchievementProgressTierGained");
                        out.writeUTF(player.getName());
                        out.writeInt(achievement.getAchievementId());
                        out.writeLong(amount);
                        out.writeInt(tierAchieved);
                        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
                    }
                    return tierAchieved;
                } else {
                    //This is just progress, no tier has been achieved
                    achievementProgress.put(achievement, achievementProgress.get(achievement) + amount);
                    if (sendToBungee) {
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("AchievementProgress");
                        out.writeUTF(player.getName());
                        out.writeInt(achievement.getAchievementId());
                        out.writeLong(amount);
                        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
                    }
                    return -1;
                }
            } else {
                if (tieredAcheivement.achievedTier(currentTier, amount) != -1) {
                    int tierAchieved = currentTier + 1;
                    while (tieredAcheivement.achievedTier(tierAchieved, amount) != -1) {
                        tierAchieved++;
                    }
                    achievementProgress.put(achievement, amount);

                    if (sendToBungee) {
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("AchievementProgressTierGained");
                        out.writeUTF(player.getName());
                        out.writeInt(achievement.getAchievementId());
                        out.writeLong(amount);
                        out.writeInt(tierAchieved);
                        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
                    }

                    return tierAchieved;
                } else {
                    //This is just progress, no tier has been achieved
                    achievementProgress.put(achievement, amount);
                    if (sendToBungee) {
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("AchievementProgress");
                        out.writeUTF(player.getName());
                        out.writeInt(achievement.getAchievementId());
                        out.writeLong(amount);
                        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
                    }
                    return -1;
                }
            }
        } else {
            achievementProgress.put(achievement, amount + ((achievementProgress.containsKey(achievement))?achievementProgress.get(achievement):0));
            if (sendToBungee) {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("AchievementProgress");
                out.writeUTF(player.getName());
                out.writeInt(achievement.getAchievementId());
                out.writeLong(amount);
                player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
            }
            return -1;
        }
    }

    public long getFirstJoinTimestamp() {
        return firstJoinTimestamp;
    }

    public long getCrownsEarned() {
        return crownsEarned;
    }

    public void addCrownsEarned(long amount, boolean sendToServer) {
        this.crownsEarned += amount;
        if (sendToServer) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("CrownsEarned");
            out.writeUTF(player.getName());
            out.writeLong(amount);
            player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
        }
    }

    public void removeCrownsEarned(long amount, boolean sendToServer) {
        this.crownsEarned -= amount;
        if (sendToServer) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("CrownsRemoved");
            out.writeUTF(player.getName());
            out.writeLong(amount);
            player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(),"BungeeCord", out.toByteArray());
        }
        new BukkitRunnable(){
            @Override
            public void run() {
                AuroraMCAPI.getDbManager().crownsEarned(player, -amount);
            }
        }.runTaskAsynchronously(AuroraMCAPI.getCore());
    }

    public long getGamesLost() {
        return gamesLost;
    }

    public void addGamePlayed(boolean win) {
        gamesPlayed++;
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        if ((win)) {
            out.writeUTF("GameWon");
            gamesWon++;
        } else {
            out.writeUTF("GameLost");
            gamesLost++;
        }
        out.writeUTF(player.getName());
        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
    }

    public long getGamesPlayed() {
        return gamesPlayed;
    }

    public long getGamesWon() {
        return gamesWon;
    }

    public long getTicketsEarned() {
        return ticketsEarned;
    }

    public void addTicketsEarned(long amount, boolean sendToServer) {
        this.ticketsEarned += amount;
        if (sendToServer) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("TicketsEarned");
            out.writeUTF(player.getName());
            out.writeLong(amount);
            player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
        }
    }

    public void removeTicketsEarned(long amount, boolean sendToServer) {
        this.ticketsEarned -= amount;
        if (sendToServer) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("TicketsRemoved");
            out.writeUTF(player.getName());
            out.writeLong(amount);
            player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(),"BungeeCord", out.toByteArray());
        }
        new BukkitRunnable(){
            @Override
            public void run() {
                AuroraMCAPI.getDbManager().ticketsEarned(player, -amount);
            }
        }.runTaskAsynchronously(AuroraMCAPI.getCore());
    }

    public void addGameTime(long ms, boolean sendToServer) {
        this.gameTimeMs += ms;
        if (sendToServer) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("GameTimeAdded");
            out.writeUTF(player.getName());
            out.writeLong(ms);
            player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(),"BungeeCord", out.toByteArray());
        }
        new BukkitRunnable(){
            @Override
            public void run() {
                AuroraMCAPI.getDbManager().addGameTime(player.getPlayer().getUniqueId(), ms);
            }
        }.runTaskAsynchronously(AuroraMCAPI.getCore());
    }

    public void addLobbyTime(long ms, boolean sendToServer) {
        this.lobbyTimeMs += ms;
        if (sendToServer) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("LobbyTimeAdded");
            out.writeUTF(player.getName());
            out.writeLong(ms);
            player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(),"BungeeCord", out.toByteArray());
        }
        new BukkitRunnable(){
            @Override
            public void run() {
                AuroraMCAPI.getDbManager().addLobbyTime(player.getPlayer().getUniqueId(), ms);
            }
        }.runTaskAsynchronously(AuroraMCAPI.getCore());
    }
}

