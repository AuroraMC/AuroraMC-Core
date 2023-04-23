/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.api.stats;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.abstraction.ScheduleFactory;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.api.utils.LevelUtils;
import net.auroramc.api.utils.Reward;
import net.auroramc.api.utils.TextFormatter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerStatistics {

    private AuroraMCPlayer player;
    private int playerId;
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


    public PlayerStatistics(AuroraMCPlayer player, int id, long firstJoinTimestamp, long totalXpEarned, long xpIntoLevel, int level, Map<Achievement, Integer> achievementsGained, Map<Achievement, Long> achievementProgress, Map<Integer, GameStatistics> stats, long lobbyTimeMs, long gameTimeMs, long gamesPlayed, long gamesWon, long gamesLost, long ticketsEarned, long crownsEarned) {
        this.firstJoinTimestamp = firstJoinTimestamp;
        this.playerId = id;
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

    public void setPlayer(AuroraMCPlayer player) {
        this.player = player;
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

    public void addLevels(int amount, boolean send) {
        level += amount;
        xpIntoLevel = 0;
        if (send) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("LevelAdd");
            out.writeUTF(player.getName());
            out.writeInt(amount);
            player.sendPluginMessage(out.toByteArray());
        }
    }
    
    public void removeLevels(int amount, boolean send) {
        level -= amount;
        xpIntoLevel = 0;
        if (send) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("LevelRemove");
            out.writeUTF(player.getName());
            out.writeInt(amount);
            player.sendPluginMessage(out.toByteArray());
        }
    }

    public boolean addXp(long amount, boolean send) {
        xpIntoLevel += amount;
        totalXpEarned += amount;
        boolean levelUp = false;

        if (LevelUtils.xpForLevel(level + 1) != -1) {
            if (xpIntoLevel >= LevelUtils.xpForLevel(level + 1)) {
                do {
                    level++;

                    if (player != null && !AuroraMCAPI.isTestServer() && send) {
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
                    if (player != null && send) {
                        TextComponent component = new TextComponent("");

                        TextComponent lines = new TextComponent("▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆");
                        lines.setBold(true);
                        lines.setColor(ChatColor.DARK_AQUA);

                        component.addExtra(lines);
                        component.addExtra("\n \n");

                        TextComponent cmp = new TextComponent("LEVEL UP! ");
                        cmp.setColor(ChatColor.DARK_AQUA);
                        cmp.setBold(true);
                        component.addExtra(cmp);

                        cmp = new TextComponent("Level " + level);
                        cmp.setColor(ChatColor.AQUA);
                        cmp.setBold(false);
                        component.addExtra(cmp);

                        cmp = new TextComponent("\n \nReward: ");
                        cmp.setColor(ChatColor.WHITE);
                        cmp.setBold(true);
                        component.addExtra(cmp);

                        cmp = new TextComponent((AuroraMCAPI.getLevelRewards().containsKey(level))?AuroraMCAPI.getLevelRewards().get(level).getRewardString():"None");
                        cmp.setColor(ChatColor.AQUA);
                        cmp.setBold(false);
                        component.addExtra(cmp);

                        component.addExtra("\n \n");

                        component.addExtra(lines);
                        player.sendMessage(lines);
                    }
                    Reward reward = AuroraMCAPI.getLevelRewards().get(level);
                    if (reward != null) {
                        if (player != null) {
                            reward.apply(player, send);
                        } else {
                            reward.apply(playerId, this);
                        }
                    }
                } while (xpIntoLevel >= LevelUtils.xpForLevel(level + 1) && LevelUtils.xpForLevel(level + 1) != -1);
                if (player != null && send) {
                    player.sendTitle(new TextComponent(TextFormatter.convert("&3&lLEVEL UP")), new TextComponent(String.format("You levelled up to level %s", level)), 10, 100, 10);
                }
                levelUp = true;
            }
        }

        if (send && !AuroraMCAPI.isTestServer()) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("XPAdd");
            out.writeUTF(player.getName());
            out.writeLong(amount);
            player.sendPluginMessage(out.toByteArray());

            ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().xpAdd(player, level, xpIntoLevel, totalXpEarned));
        }
        return levelUp;
    }

    public void removeXp(long amount, boolean send) {
        final long totalRemoved = amount;
        totalXpEarned -= amount;
        if (amount > xpIntoLevel) {
            do {
                level--;
                amount -= xpIntoLevel;
                xpIntoLevel = LevelUtils.xpForLevel(level + 1);
            } while (amount > xpIntoLevel && LevelUtils.xpForLevel(level) != -1);
        }
        xpIntoLevel -= amount;

        if (player != null) {
            if (!AuroraMCAPI.isTestServer()) {
                ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().xpAdd(player, level, xpIntoLevel, totalXpEarned));
            }
        }
        if (send) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("XPRemove");
            out.writeUTF(player.getName());
            out.writeLong(totalRemoved);
            player.sendPluginMessage(out.toByteArray());
        }
    }

    public void incrementStatistic(int gameId, String key, long amount, boolean send) {
        if (stats.containsKey(gameId)) {
            stats.get(gameId).addStat(key, amount);
        } else {
            Map<String, Long> map = new HashMap<>();
            map.put(key, amount);
            stats.put(gameId, new GameStatistics(gameId, map));
        }

        if (player != null && send) {
            if (!AuroraMCAPI.isTestServer()) {
                ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().statisticIncrement(player, gameId, key, amount));
            }
        }

        if (send && !AuroraMCAPI.isTestServer()) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("StatisticIncrement");
            out.writeUTF(player.getName());
            out.writeInt(gameId);
            out.writeUTF(key);
            out.writeLong(amount);

            player.sendPluginMessage(out.toByteArray());
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

    public void achievementGained(Achievement achievement, int tier, boolean send) {
        if (achievementsGained.get(achievement) != null) {
            if (tier > achievementsGained.get(achievement) && send) {
                if (player != null) {
                    TextComponent component = new TextComponent("");

                    TextComponent lines = new TextComponent("▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆");
                    lines.setBold(true);
                    lines.setColor(ChatColor.DARK_AQUA);

                    component.addExtra(lines);
                    component.addExtra("\n \n");

                    TextComponent cmp = new TextComponent("Achievement Get: ");
                    cmp.setColor(ChatColor.DARK_AQUA);
                    cmp.setBold(true);
                    component.addExtra(cmp);

                    if (achievement instanceof TieredAcheivement) {
                        cmp = new TextComponent(achievement.getName() + " Tier " + tier);
                        cmp.setColor(ChatColor.AQUA);
                        cmp.setBold(false);
                        component.addExtra(cmp);
                        ((TieredAcheivement) achievement).getTier(tier).getReward().apply(player, send);
                    } else {
                        cmp = new TextComponent(achievement.getName());
                        cmp.setColor(ChatColor.AQUA);
                        cmp.setBold(false);
                        component.addExtra(cmp);
                    }
                    component.addExtra("\n \n");
                    cmp = new TextComponent(achievement.getDescription());
                    cmp.setColor(ChatColor.WHITE);
                    cmp.setBold(false);
                    component.addExtra(cmp);

                    cmp = new TextComponent("\nReward: ");
                    cmp.setColor(ChatColor.WHITE);
                    cmp.setBold(true);
                    component.addExtra(cmp);

                    cmp = new TextComponent(TextComponent.fromLegacyText(TextFormatter.convert(achievement.getRewards().getRewardString())));
                    cmp.setColor(ChatColor.WHITE);
                    cmp.setBold(false);
                    component.addExtra(cmp);

                    component.addExtra("\n \n");

                    component.addExtra(lines);

                    player.sendMessage(component);

                    achievement.getRewards().apply(player, send);
                } else {
                    achievement.getRewards().apply(playerId, this);
                    if (achievement instanceof TieredAcheivement) {
                        ((TieredAcheivement) achievement).getTier(tier).getReward().apply(playerId, this);
                    }
                }
            }
        } else if (send) {
            if (player != null) {
                TextComponent component = new TextComponent("");

                TextComponent lines = new TextComponent("▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆");
                lines.setBold(true);
                lines.setColor(ChatColor.DARK_AQUA);

                component.addExtra(lines);
                component.addExtra("\n \n");

                TextComponent cmp = new TextComponent("Achievement Get: ");
                cmp.setColor(ChatColor.DARK_AQUA);
                cmp.setBold(true);
                component.addExtra(cmp);

                if (achievement instanceof TieredAcheivement) {
                    cmp = new TextComponent(achievement.getName() + " Tier " + tier);
                    cmp.setColor(ChatColor.AQUA);
                    cmp.setBold(false);
                    component.addExtra(cmp);
                    ((TieredAcheivement) achievement).getTier(tier).getReward().apply(player, send);
                } else {
                    cmp = new TextComponent(achievement.getName());
                    cmp.setColor(ChatColor.AQUA);
                    cmp.setBold(false);
                    component.addExtra(cmp);
                }
                component.addExtra("\n \n");
                cmp = new TextComponent(achievement.getDescription());
                cmp.setColor(ChatColor.WHITE);
                cmp.setBold(false);
                component.addExtra(cmp);

                cmp = new TextComponent("\nReward: ");
                cmp.setColor(ChatColor.WHITE);
                cmp.setBold(true);
                component.addExtra(cmp);

                cmp = new TextComponent(TextComponent.fromLegacyText(TextFormatter.convert(achievement.getRewards().getRewardString())));
                cmp.setColor(ChatColor.WHITE);
                cmp.setBold(false);
                component.addExtra(cmp);

                component.addExtra("\n \n");

                component.addExtra(lines);

                player.sendMessage(component);

                achievement.getRewards().apply(player, send);
            } else {
                achievement.getRewards().apply(playerId, this);
                if (achievement instanceof TieredAcheivement) {
                    ((TieredAcheivement) achievement).getTier(tier).getReward().apply(playerId, this);
                }
            }
        }
        achievementsGained.put(achievement, tier);
        if (player != null && send) {
            if (!AuroraMCAPI.isTestServer()) {
                ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().achievementGet(player, achievement, tier));
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("AchievementGained");
                out.writeUTF(player.getName());
                out.writeInt(achievement.getAchievementId());
                out.writeInt(tier);
                player.sendPluginMessage(out.toByteArray());
            }
        }
    }

    public void achievementRemoved(Achievement achievement, boolean send) {
        achievementsGained.remove(achievement);
        if (send) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("AchievementRemoved");
            out.writeUTF(player.getName());
            out.writeInt(achievement.getAchievementId());
            player.sendPluginMessage(out.toByteArray());
        }
    }

    public Map<Achievement, Long> getAchievementProgress() {
        return new HashMap<>(achievementProgress);
    }

    public void setAchievementProgress(Achievement achievement, long amount, boolean send) {
        if (amount == 0) {
            achievementProgress.remove(achievement);
        } else {
            achievementProgress.put(achievement, amount);
        }

        if (send) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("AchievementProgressSet");
            out.writeUTF(player.getName());
            out.writeInt(achievement.getAchievementId());
            out.writeLong(amount);
            player.sendPluginMessage(out.toByteArray());
        }
    }

    public int addProgress(Achievement achievement, long amount, int currentTier, boolean send) {
        if (player != null && send) {
            if (!AuroraMCAPI.isTestServer()) {
                ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().achievementProgress(player, achievement, amount));
            }
        }
        if (achievement instanceof TieredAcheivement) {
            TieredAcheivement tieredAcheivement = (TieredAcheivement) achievement;
            if (achievementProgress.containsKey(achievement)) {
                if (tieredAcheivement.achievedTier(currentTier, amount + achievementProgress.get(achievement)) != -1) {
                    int tierAchieved = currentTier;
                    do {
                        tierAchieved++;
                    } while (tieredAcheivement.achievedTier(tierAchieved, amount + achievementProgress.get(achievement)) != -1);
                    achievementProgress.put(achievement, achievementProgress.get(achievement) + amount);
                    achievementsGained.put(achievement, tierAchieved);
                    if (player != null && send) {
                        TextComponent component = new TextComponent("");

                        TextComponent lines = new TextComponent("▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆");
                        lines.setBold(true);
                        lines.setColor(ChatColor.DARK_AQUA);

                        component.addExtra(lines);
                        component.addExtra("\n \n");

                        TextComponent cmp = new TextComponent("Achievement Get: ");
                        cmp.setColor(ChatColor.DARK_AQUA);
                        cmp.setBold(true);
                        component.addExtra(cmp);

                        cmp = new TextComponent(achievement.getName() + " Tier " + tierAchieved);
                        cmp.setColor(ChatColor.AQUA);
                        cmp.setBold(false);
                        component.addExtra(cmp);


                        component.addExtra("\n \n");
                        cmp = new TextComponent(achievement.getDescription());
                        cmp.setColor(ChatColor.WHITE);
                        cmp.setBold(false);
                        component.addExtra(cmp);

                        cmp = new TextComponent("\nReward: ");
                        cmp.setColor(ChatColor.WHITE);
                        cmp.setBold(true);
                        component.addExtra(cmp);

                        cmp = new TextComponent(TextComponent.fromLegacyText(TextFormatter.convert(achievement.getRewards().getRewardString())));
                        cmp.setColor(ChatColor.WHITE);
                        cmp.setBold(false);
                        component.addExtra(cmp);

                        component.addExtra("\n \n");

                        component.addExtra(lines);

                        player.sendMessage(component);
                        if (!AuroraMCAPI.isTestServer()) {
                            AuroraMCAPI.getDbManager().achievementGet(player, achievement, tierAchieved);
                        }
                        tieredAcheivement.getTier(tierAchieved).getReward().apply(player, send);
                        tieredAcheivement.getRewards().apply(player, send);
                    } else {
                        achievement.getRewards().apply(playerId, this);
                        tieredAcheivement.getTier(tierAchieved).getReward().apply(playerId, this);
                    }
                    if (send) {
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("AchievementProgressTierGained");
                        out.writeUTF(player.getName());
                        out.writeInt(achievement.getAchievementId());
                        out.writeLong(amount);
                        out.writeInt(tierAchieved);
                        player.sendPluginMessage(out.toByteArray());
                    }
                    return tierAchieved;
                } else {
                    //This is just progress, no tier has been achieved
                    achievementProgress.put(achievement, achievementProgress.get(achievement) + amount);
                    if (send) {
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("AchievementProgress");
                        out.writeUTF(player.getName());
                        out.writeInt(achievement.getAchievementId());
                        out.writeLong(amount);
                        player.sendPluginMessage(out.toByteArray());
                    }
                    return -1;
                }
            } else {
                if (tieredAcheivement.achievedTier(currentTier, amount) != -1) {
                    int tierAchieved = currentTier;
                    do {
                        tierAchieved++;
                    } while (tieredAcheivement.achievedTier(tierAchieved, amount) != -1);
                    achievementProgress.put(achievement, amount);
                    achievementsGained.put(achievement, tierAchieved);
                    if (player != null && send) {
                        TextComponent component = new TextComponent("");

                        TextComponent lines = new TextComponent("▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆");
                        lines.setBold(true);
                        lines.setColor(ChatColor.DARK_AQUA);

                        component.addExtra(lines);
                        component.addExtra("\n \n");

                        TextComponent cmp = new TextComponent("Achievement Get: ");
                        cmp.setColor(ChatColor.DARK_AQUA);
                        cmp.setBold(true);
                        component.addExtra(cmp);

                        cmp = new TextComponent(achievement.getName() + " Tier " + tierAchieved);
                        cmp.setColor(ChatColor.AQUA);
                        cmp.setBold(false);
                        component.addExtra(cmp);


                        component.addExtra("\n \n");
                        cmp = new TextComponent(achievement.getDescription());
                        cmp.setColor(ChatColor.WHITE);
                        cmp.setBold(false);
                        component.addExtra(cmp);

                        cmp = new TextComponent("\nReward: ");
                        cmp.setColor(ChatColor.WHITE);
                        cmp.setBold(true);
                        component.addExtra(cmp);

                        cmp = new TextComponent(TextComponent.fromLegacyText(TextFormatter.convert(achievement.getRewards().getRewardString())));
                        cmp.setColor(ChatColor.WHITE);
                        cmp.setBold(false);
                        component.addExtra(cmp);

                        component.addExtra("\n \n");

                        component.addExtra(lines);

                        player.sendMessage(component);
                        if (!AuroraMCAPI.isTestServer()) {
                            AuroraMCAPI.getDbManager().achievementGet(player, achievement, tierAchieved);
                        }
                        tieredAcheivement.getTier(tierAchieved).getReward().apply(player, send);
                        tieredAcheivement.getRewards().apply(player, send);
                    } else {
                        achievement.getRewards().apply(playerId, this);
                        tieredAcheivement.getTier(tierAchieved).getReward().apply(playerId, this);
                    }
                    if (send) {
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("AchievementProgressTierGained");
                        out.writeUTF(player.getName());
                        out.writeInt(achievement.getAchievementId());
                        out.writeLong(amount);
                        out.writeInt(tierAchieved);
                        player.sendPluginMessage(out.toByteArray());
                    }
                    return tierAchieved;
                } else {
                    //This is just progress, no tier has been achieved
                    achievementProgress.put(achievement, amount);
                    if (send) {
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("AchievementProgress");
                        out.writeUTF(player.getName());
                        out.writeInt(achievement.getAchievementId());
                        out.writeLong(amount);
                        player.sendPluginMessage(out.toByteArray());
                    }
                    return -1;
                }
            }
        } else {
            achievementProgress.put(achievement, amount + ((achievementProgress.containsKey(achievement))?achievementProgress.get(achievement):0));
            if (send) {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("AchievementProgress");
                out.writeUTF(player.getName());
                out.writeInt(achievement.getAchievementId());
                out.writeLong(amount);
                player.sendPluginMessage(out.toByteArray());
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

    public void addCrownsEarned(long amount, boolean send) {
        this.crownsEarned += amount;
        if (send && !AuroraMCAPI.isTestServer()) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("CrownsEarned");
            out.writeUTF(player.getName());
            out.writeLong(amount);
            player.sendPluginMessage(out.toByteArray());
            ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().crownsEarned(player, amount));
        }
    }

    public void removeCrownsEarned(long amount, boolean send) {
        this.crownsEarned -= amount;
        if (send && !AuroraMCAPI.isTestServer()) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("CrownsRemoved");
            out.writeUTF(player.getName());
            out.writeLong(amount);
            player.sendPluginMessage(out.toByteArray());
            ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().crownsEarned(player, -amount));
        }
    }

    public long getGamesLost() {
        return gamesLost;
    }

    public void addGamePlayed(boolean win, boolean send) {
        gamesPlayed++;
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        if ((win)) {
            out.writeUTF("GameWon");
            gamesWon++;
        } else {
            out.writeUTF("GameLost");
            gamesLost++;
        }
        if (AuroraMCAPI.isTestServer()) {
            return;
        }
        out.writeUTF(player.getName());
        if (send) {
            player.sendPluginMessage(out.toByteArray());
            ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().gamePlayed(player, win));
        }
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

    public void addTicketsEarned(long amount, boolean send) {
        this.ticketsEarned += amount;
        if (send && !AuroraMCAPI.isTestServer()) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("TicketsEarned");
            out.writeUTF(player.getName());
            out.writeLong(amount);
            player.sendPluginMessage(out.toByteArray());
            ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().ticketsEarned(player, amount));
        }
    }

    public void removeTicketsEarned(long amount, boolean send) {
        this.ticketsEarned -= amount;
        if (send && !AuroraMCAPI.isTestServer()) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("TicketsRemoved");
            out.writeUTF(player.getName());
            out.writeLong(amount);
            player.sendPluginMessage(out.toByteArray());
            ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().ticketsEarned(player, -amount));
        }
    }

    public void addGameTime(long ms, boolean send) {
        this.gameTimeMs += ms;
        if (send && !AuroraMCAPI.isTestServer()) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("GameTimeAdded");
            out.writeUTF(player.getName());
            out.writeLong(ms);
            player.sendPluginMessage(out.toByteArray());
            ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().addGameTime(player.getUniqueId(), ms));
        }
    }

    public void addLobbyTime(long ms, boolean send) {
        this.lobbyTimeMs += ms;
        if (send && !AuroraMCAPI.isTestServer()) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("LobbyTimeAdded");
            out.writeUTF(player.getName());
            out.writeLong(ms);
            player.sendPluginMessage(out.toByteArray());
            ScheduleFactory.scheduleAsync(() -> AuroraMCAPI.getDbManager().addLobbyTime(player.getUniqueId(), ms));
        }
    }

    public String toJSON() {
        String json = "{\"firstJoinTimestamp\":\"%s\",\"totalXpEarned\":\"%s\",\"xpIntoLevel\":\"%s\",\"level\":\"%s\",\"lobbyTimeMs\":\"%s\",\"gameTimeMs\":\"%s\",\"gamesPlayed\":\"%s\",\"gamesWon\":\"%s\",\"gamesLost\":\"%s\",\"ticketsEarned\":\"%s\",\"crownsEarned\":\"%s\",\"achievementsGained\":[%s],\"achievementProgress\":[%s],\"stats\":[%s]}";

        List<String> achievements = new ArrayList<>();
        for (Map.Entry<Achievement, Integer> entry : this.achievementsGained.entrySet()) {
            achievements.add(String.format("{\"%s\":%s}", entry.getKey().getAchievementId(), entry.getValue()));
        }
        String achievementsGained = String.join(",", achievements);

        List<String> achieveProgress = new ArrayList<>();
        for (Map.Entry<Achievement, Long> entry : this.achievementProgress.entrySet()) {
            achieveProgress.add(String.format("{\"%s\":\"%s\"}", entry.getKey().getAchievementId(), entry.getValue()));
        }
        String achievementProgress = String.join(",", achieveProgress);

        List<String> stats = new ArrayList<>();
        for (Map.Entry<Integer, GameStatistics> entry : this.stats.entrySet()) {
            List<String> gameStats = new ArrayList<>();
            for (Map.Entry<String, Long> gameStat : entry.getValue().getStats().entrySet()) {
                gameStats.add(String.format("{\"%s\":\"%s\"}", gameStat.getKey(), gameStat.getValue()));
            }
            stats.add(String.join(",",gameStats));
        }
        String gameStats = String.join(",", stats);

        json = String.format(json, firstJoinTimestamp, totalXpEarned, xpIntoLevel, level, lobbyTimeMs, gameTimeMs, gamesPlayed, gamesWon, gamesLost, ticketsEarned, crownsEarned, achievementsGained, achievementProgress, gameStats);
        return json;
    }

}

