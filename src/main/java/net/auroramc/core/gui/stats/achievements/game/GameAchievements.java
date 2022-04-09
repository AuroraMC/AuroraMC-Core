/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.gui.stats.achievements.game;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.stats.Achievement;
import net.auroramc.core.api.stats.PlayerStatistics;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.core.gui.stats.achievements.Achievements;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class GameAchievements extends GUI {

    private final AuroraMCPlayer player;
    private final String name;
    private final PlayerStatistics stats;

    public GameAchievements(AuroraMCPlayer player, String targetName, PlayerStatistics targetStatistics, ItemStack item) {
        super("&3&l" + targetName + "'s Statistics", 5, true);

        this.player = player;
        this.name = targetName;
        this.stats = targetStatistics;

        border(String.format("&3&l%s's Statistics", name), "");
        this.setItem(0, 4, new GUIItem(item));
        this.setItem(0, 0, new GUIItem(Material.ARROW, "&3&lBACK"));

        long totalCrystalAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 1).filter((Achievement::isVisible)).count() + targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 1).filter((achievement -> !achievement.isVisible())).count();
        long totalBackstabAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 2).filter((Achievement::isVisible)).count() + targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 2).filter((achievement -> !achievement.isVisible())).count();
        long totalPaintballAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 3).filter((Achievement::isVisible)).count() + targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 3).filter((achievement -> !achievement.isVisible())).count();
        long totalSpleefAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 100).filter((Achievement::isVisible)).count() + targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 100).filter((achievement -> !achievement.isVisible())).count();
        long totalHotPotatoAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 101).filter((Achievement::isVisible)).count() + targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 101).filter((achievement -> !achievement.isVisible())).count();
        long totalFFAAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 102).filter((Achievement::isVisible)).count() + targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 102).filter((achievement -> !achievement.isVisible())).count();
        long totalHITWAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 103).filter((Achievement::isVisible)).count() + targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 103).filter((achievement -> !achievement.isVisible())).count();
        long totalBlockPartyAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 104).filter((Achievement::isVisible)).count() + targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 104).filter((achievement -> !achievement.isVisible())).count();

        this.setItem(2, 2, new GUIItem(Material.NETHER_STAR, "&b&lCrystal Quest", 1, String.format("&rAchieved: **%s**;&rTotal Achievements: **%s**", targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 1).count(), totalCrystalAchievements)));
        this.setItem(2, 3, new GUIItem(Material.IRON_SWORD, "&c&lBackstab", 1, String.format("&rAchieved: **%s**;&rTotal Achievements: **%s**", targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 2).count(), totalBackstabAchievements)));
        this.setItem(2, 4, new GUIItem(Material.SNOW_BALL, "&a&lPaintball", 1, String.format("&rAchieved: **%s**;&rTotal Achievements: **%s**", targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 3).count(), totalPaintballAchievements)));
        this.setItem(2, 5, new GUIItem(Material.IRON_SPADE, "&b&lSpleef", 1, String.format("&rAchieved: **%s**;&rTotal Achievements: **%s**", targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 100).count(), totalSpleefAchievements)));
        this.setItem(2, 6, new GUIItem(Material.BAKED_POTATO, "&c&lHotPotato", 1, String.format("&rAchieved: **%s**;&rTotal Achievements: **%s**", targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 101).count(), totalHotPotatoAchievements)));
        this.setItem(3, 3, new GUIItem(Material.IRON_AXE, "&c&lFFA", 1, String.format("&rAchieved: **%s**;&rTotal Achievements: **%s**", targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 102).count(), totalFFAAchievements)));
        this.setItem(3, 4, new GUIItem(Material.COBBLE_WALL, "&c&lHole In The Wall", 1, String.format("&rAchieved: **%s**;&rTotal Achievements: **%s**", targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 103).count(), totalHITWAchievements)));
        this.setItem(3, 5, new GUIItem(Material.NOTE_BLOCK, AuroraMCAPI.getFormatter().rainbowBold("Block Party"), 1, String.format("&rAchieved: **%s**;&rTotal Achievements: **%s**", targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 104).count(), totalBlockPartyAchievements)));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (item.getType() == Material.STAINED_GLASS_PANE) {
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
            return;
        }
        if (item.getType() == Material.ARROW && column == 0) {
            Achievements achievements = new Achievements(player, stats, name);
            AuroraMCAPI.closeGUI(player);
            achievements.open(player);
            AuroraMCAPI.openGUI(player, achievements);
            return;
        }
        switch (item.getType()) {
            case NETHER_STAR: {
                AuroraMCAPI.closeGUI(player);
                GameAchievementListing stats = new GameAchievementListing(player, this.stats, name, item, 1, "Crystal Quest");
                stats.open(player);
                AuroraMCAPI.openGUI(player, stats);
                break;
            }
            case IRON_AXE: {
                AuroraMCAPI.closeGUI(player);
                GameAchievementListing stats = new GameAchievementListing(player, this.stats, name, item, 102, "FFA");
                stats.open(player);
                AuroraMCAPI.openGUI(player, stats);
                break;
            }
            case IRON_SPADE: {
                AuroraMCAPI.closeGUI(player);
                GameAchievementListing stats = new GameAchievementListing(player, this.stats, name, item, 100, "Spleef");
                stats.open(player);
                AuroraMCAPI.openGUI(player, stats);
                break;
            }
            case BAKED_POTATO: {
                AuroraMCAPI.closeGUI(player);
                GameAchievementListing stats = new GameAchievementListing(player, this.stats, name, item, 101, "Hot Potato");
                stats.open(player);
                AuroraMCAPI.openGUI(player, stats);
                break;
            }
            default: {
                player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
            }
        }
    }
}
