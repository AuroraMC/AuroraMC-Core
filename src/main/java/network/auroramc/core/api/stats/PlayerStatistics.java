package network.auroramc.core.api.stats;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.players.AuroraMCPlayer;
import network.auroramc.core.api.utils.LevelUtils;

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

    public PlayerStatistics(AuroraMCPlayer player, long firstJoinTimestamp, long totalXpEarned, long xpIntoLevel, int level, Map<Achievement, Integer> achievementsGained, Map<Achievement, Long> achievementProgress, Map<Integer, GameStatistics> stats, long lobbyTimeMs, long gameTimeMs) {
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

    public boolean addXp(int amount) {
        xpIntoLevel += amount;
        totalXpEarned += amount;
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        boolean levelUp = false;

        if (xpIntoLevel > LevelUtils.xpForLevel(level + 1)) {
            do {
                level++;
                xpIntoLevel = (xpIntoLevel - LevelUtils.xpForLevel(level));
            } while (xpIntoLevel > LevelUtils.xpForLevel(level + 1));
            levelUp = true;
        }
        out.writeUTF("XPAdd");
        out.writeUTF(player.getName());
        out.writeLong(amount);
        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
        return levelUp;
    }

    public void incrementStatistic(int gameId, String key, long amount) {
        if (stats.containsKey(gameId)) {
            stats.get(gameId).addStat(key, amount);
        } else {
            Map<String, Long> map = new HashMap<>();
            map.put(key, amount);
            stats.put(gameId, new GameStatistics(gameId, map));
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("StatisticIncrement");
        out.writeUTF(player.getName());
        out.writeInt(gameId);
        out.writeUTF(key);
        out.writeLong(amount);

        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());

    }

    public long getStatistic(int gameId, String key) {
        if (stats.get(gameId) == null) {
            return -1;
        }

        return stats.get(gameId).getStat(key);
    }

    public GameStatistics getGameStatistics(int gameId) {
        return stats.get(gameId);
    }

    public void achievementGained(Achievement achievement, int tier) {
        achievementsGained.put(achievement, tier);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("AchievementGained");
        out.writeUTF(player.getName());
        out.writeInt(achievement.getAchievementId());
        out.writeInt(tier);
        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
    }

    public Map<Achievement, Long> getAchievementProgress() {
        return new HashMap<>(achievementProgress);
    }

    public int addProgress(Achievement achievement, long amount, int currentTier) {
        if (achievement instanceof TieredAcheivement) {
            TieredAcheivement tieredAcheivement = (TieredAcheivement) achievement;
            if (achievementProgress.containsKey(achievement)) {
                if (tieredAcheivement.achievedTier(currentTier, amount + achievementProgress.get(achievement)) != -1) {
                    int tierAchieved = currentTier + 1;
                    while (tieredAcheivement.achievedTier(tierAchieved, amount + achievementProgress.get(achievement)) != -1) {
                        tierAchieved++;
                    }
                    achievementProgress.put(achievement, achievementProgress.get(achievement) + amount);

                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("AchievementProgressTierGained");
                    out.writeUTF(player.getName());
                    out.writeInt(achievement.getAchievementId());
                    out.writeLong(amount);
                    out.writeInt(tierAchieved);
                    player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
                    return tierAchieved;
                } else {
                    //This is just progress, no tier has been achieved
                    achievementProgress.put(achievement, achievementProgress.get(achievement) + amount);
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("AchievementProgress");
                    out.writeUTF(player.getName());
                    out.writeInt(achievement.getAchievementId());
                    out.writeLong(amount);
                    player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
                    return -1;
                }
            } else {
                if (tieredAcheivement.achievedTier(currentTier, amount) != -1) {
                    int tierAchieved = currentTier + 1;
                    while (tieredAcheivement.achievedTier(tierAchieved, amount) != -1) {
                        tierAchieved++;
                    }
                    achievementProgress.put(achievement, amount);

                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("AchievementProgressTierGained");
                    out.writeUTF(player.getName());
                    out.writeInt(achievement.getAchievementId());
                    out.writeLong(amount);
                    out.writeInt(tierAchieved);
                    player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());

                    return tierAchieved;
                } else {
                    //This is just progress, no tier has been achieved
                    achievementProgress.put(achievement, amount);
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("AchievementProgress");
                    out.writeUTF(player.getName());
                    out.writeInt(achievement.getAchievementId());
                    out.writeLong(amount);
                    player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
                    return -1;
                }
            }
        } else {
            achievementProgress.put(achievement, amount + ((achievementProgress.containsKey(achievement))?achievementProgress.get(achievement):0));
            return -1;
        }
    }
}

