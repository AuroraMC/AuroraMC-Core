/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.gui.stats.achievements.game;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.stats.Achievement;
import net.auroramc.api.stats.PlayerStatistics;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import net.auroramc.smp.api.utils.gui.GUI;
import net.auroramc.smp.api.utils.gui.GUIItem;
import net.auroramc.smp.gui.stats.achievements.Achievements;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class GameAchievements extends GUI {

    private final AuroraMCServerPlayer player;
    private final String name;
    private final PlayerStatistics stats;

    public GameAchievements(AuroraMCServerPlayer player, String targetName, PlayerStatistics targetStatistics, ItemStack item) {
        super("&3&l" + targetName + "'s Statistics", 5, true);

        this.player = player;
        this.name = targetName;
        this.stats = targetStatistics;

        border(String.format("&3&l%s's Statistics", name), "");
        this.setItem(0, 4, new GUIItem(item));
        this.setItem(0, 0, new GUIItem(Material.ARROW, "&3&lBACK"));

        long totalCrystalAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 1).filter((Achievement::isVisible)).count() + targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 1).filter((achievement -> !achievement.isVisible())).count();
        long totalDuelsAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 4).filter((Achievement::isVisible)).count() + targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 4).filter((achievement -> !achievement.isVisible())).count();
        long totalPaintballAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 3).filter((Achievement::isVisible)).count() + targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 3).filter((achievement -> !achievement.isVisible())).count();
        long totalSpleefAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 100).filter((Achievement::isVisible)).count() + targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 100).filter((achievement -> !achievement.isVisible())).count();
        long totalHotPotatoAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 101).filter((Achievement::isVisible)).count() + targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 101).filter((achievement -> !achievement.isVisible())).count();
        long totalFFAAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 102).filter((Achievement::isVisible)).count() + targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 102).filter((achievement -> !achievement.isVisible())).count();
        long totalTagAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 103).filter((Achievement::isVisible)).count() + targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 103).filter((achievement -> !achievement.isVisible())).count();
        long totalRunAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 104).filter((Achievement::isVisible)).count() + targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 104).filter((achievement -> !achievement.isVisible())).count();

        this.setItem(2, 2, new GUIItem(Material.NETHER_STAR, "&b&lCrystal Quest", 1, String.format("&r&fAchieved: **%s**;&r&fTotal Achievements: **%s**", targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 1).count(), totalCrystalAchievements)));
        this.setItem(2, 3, new GUIItem(Material.IRON_SWORD, "&c&lDuels", 1, String.format("&r&fAchieved: **%s**;&r&fTotal Achievements: **%s**", targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 4).count(), totalDuelsAchievements)));
        this.setItem(2, 4, new GUIItem(Material.SNOWBALL, "&a&lPaintball", 1, String.format("&r&fAchieved: **%s**;&r&fTotal Achievements: **%s**", targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 3).count(), totalPaintballAchievements)));
        this.setItem(2, 5, new GUIItem(Material.IRON_SHOVEL, "&b&lSpleef", 1, String.format("&r&fAchieved: **%s**;&r&fTotal Achievements: **%s**", targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 100).count(), totalSpleefAchievements)));
        this.setItem(2, 6, new GUIItem(Material.BAKED_POTATO, "&c&lHot Potato", 1, String.format("&r&fAchieved: **%s**;&r&fTotal Achievements: **%s**", targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 101).count(), totalHotPotatoAchievements)));
        this.setItem(3, 3, new GUIItem(Material.IRON_AXE, "&c&lFFA", 1, String.format("&r&fAchieved: **%s**;&r&fTotal Achievements: **%s**", targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 102).count(), totalFFAAchievements)));
        this.setItem(3, 4, new GUIItem(Material.LEAD, "&c&lTag", 1, String.format("&r&fAchieved: **%s**;&r&fTotal Achievements: **%s**", targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 103).count(), totalTagAchievements)));
        this.setItem(3, 5, new GUIItem(Material.RED_CONCRETE, "&e&lRun", 1, String.format("&r&fAchieved: **%s**;&r&fTotal Achievements: **%s**", targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 104).count(), totalRunAchievements)));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (item.getType() == Material.GRAY_STAINED_GLASS_PANE) {
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 100, 0);
            return;
        }
        if (item.getType() == Material.ARROW && column == 0) {
            Achievements achievements = new Achievements(player, stats, name);
            achievements.open(player);
            return;
        }
        switch (item.getType()) {
            case NETHER_STAR: {
                GameAchievementListing stats = new GameAchievementListing(player, this.stats, name, item, 1, "Crystal Quest");
                stats.open(player);
                break;
            }
            case IRON_AXE: {
                GameAchievementListing stats = new GameAchievementListing(player, this.stats, name, item, 102, "FFA");
                stats.open(player);
                break;
            }
            case IRON_SWORD: {
                GameAchievementListing stats = new GameAchievementListing(player, this.stats, name, item, 4, "Duels");
                stats.open(player);
                break;
            }
            case IRON_SHOVEL: {
                GameAchievementListing stats = new GameAchievementListing(player, this.stats, name, item, 100, "Spleef");
                stats.open(player);
                break;
            }
            case BAKED_POTATO: {
                GameAchievementListing stats = new GameAchievementListing(player, this.stats, name, item, 101, "Hot Potato");
                stats.open(player);
                break;
            }
            case LEAD: {
                GameAchievementListing stats = new GameAchievementListing(player, this.stats, name, item, 103, "Tag");
                stats.open(player);
                break;
            }
            case RED_CONCRETE: {
                GameAchievementListing stats = new GameAchievementListing(player, this.stats, name, item, 104, "Run");
                stats.open(player);
                break;
            }
            default: {
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 100, 0);
            }
        }
    }
}
