/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.gui.stats.achievements;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.stats.Achievement;
import net.auroramc.api.stats.PlayerStatistics;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.core.gui.stats.achievements.game.GameAchievements;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class Achievements extends GUI {

    private final AuroraMCServerPlayer player;
    private final PlayerStatistics targetStatistics;
    private final String target;

    public Achievements(AuroraMCServerPlayer player, PlayerStatistics targetStatistics, String target) {
        super("&3&lAchievements", 5, true);

        this.player = player;
        this.targetStatistics = targetStatistics;
        this.target = target;

        long totalAchievements = AuroraMCAPI.getAchievements().values().stream().filter((Achievement::isVisible)).count() + targetStatistics.getAchievementsGained().keySet().stream().filter((achievement -> !achievement.isVisible())).count();

        border(String.format("&3&l%s's Achievements", target), "");
        this.setItem(0, 4, new GUIItem(Material.PLAYER_HEAD, String.format("&3&l%s's Achievements", target), 1, String.format(";&r&fAchieved: **%s**;&r&fTotal Achievements: **%s**", targetStatistics.getAchievementsGained().size(), totalAchievements), (short) 0, false, target));

        long totalGeneralAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GENERAL).filter((Achievement::isVisible)).count() + targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GENERAL).filter((achievement -> !achievement.isVisible())).count();
        long totalFriendsAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.FRIENDS).filter((Achievement::isVisible)).count() + targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.FRIENDS).filter((achievement -> !achievement.isVisible())).count();
        long totalPartyAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.PARTY).filter((Achievement::isVisible)).count() + targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.PARTY).filter((achievement -> !achievement.isVisible())).count();
        long totalTimeAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.TIME).filter((Achievement::isVisible)).count() + targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.TIME).filter((achievement -> !achievement.isVisible())).count();
        long totalLoyaltyAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.LOYALTY).filter((Achievement::isVisible)).count() + targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.LOYALTY).filter((achievement -> !achievement.isVisible())).count();
        long totalExperienceAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.EXPERIENCE).filter((Achievement::isVisible)).count() + targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.EXPERIENCE).filter((achievement -> !achievement.isVisible())).count();
        long totalGameAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME).filter((Achievement::isVisible)).count() + targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME).filter((achievement -> !achievement.isVisible())).count();
        long totalLobbyAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.LOBBY).filter((Achievement::isVisible)).count() + targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.LOBBY).filter((achievement -> !achievement.isVisible())).count();

        this.setItem(2, 2, new GUIItem(Material.OAK_SIGN, "&3&lGeneral Achievements", 1, String.format("&r&fAchieved: **%s**;&r&fTotal Achievements: **%s**", targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GENERAL).count(), totalGeneralAchievements)));
        this.setItem(2, 3, new GUIItem(Material.SKELETON_SKULL, "&d&lFriends Achievements", 1, String.format("&r&fAchieved: **%s**;&r&fTotal Achievements: **%s**", targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.FRIENDS).count(), totalFriendsAchievements)));
        this.setItem(2, 4, new GUIItem(Material.FIREWORK_ROCKET, "&6&lParty Achievements", 1, String.format("&r&fAchieved: **%s**;&r&fTotal Achievements: **%s**", targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.PARTY).count(), totalPartyAchievements)));
        this.setItem(2, 5, new GUIItem(Material.CLOCK, "&c&lTime Achievements", 1, String.format("&r&fAchieved: **%s**;&r&fTotal Achievements: **%s**", targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.TIME).count(), totalTimeAchievements)));
        this.setItem(2, 6, new GUIItem(Material.ROSE_BUSH, "&a&lLoyalty Achievements", 1, String.format("&r&fAchieved: **%s**;&r&fTotal Achievements: **%s**", targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.LOYALTY).count(), totalLoyaltyAchievements)));
        this.setItem(3, 3, new GUIItem(Material.EXPERIENCE_BOTTLE, "&b&lExperience Achievements", 1, String.format("&r&fAchieved: **%s**;&r&fTotal Achievements: **%s**", targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.EXPERIENCE).count(), totalExperienceAchievements)));
        this.setItem(3, 4, new GUIItem(Material.MINECART, "&e&lGame Achievements", 1, String.format("&r&fAchieved: **%s**;&r&fTotal Achievements: **%s**", targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME).count(), totalGameAchievements)));
        this.setItem(3, 5, new GUIItem(Material.BEACON, "&4&lLobby Achievements", 1, String.format("&r&fAchieved: **%s**;&r&fTotal Achievements: **%s**", targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.LOBBY).count(), totalLobbyAchievements)));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        Achievement.AchievementCategory category;
        switch (item.getType()) {
            case OAK_SIGN:
                category = Achievement.AchievementCategory.GENERAL;
                break;
            case PLAYER_HEAD:
                if (row == 0 && column == 4) {
                    player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 100, 0);
                    return;
                }
                category = Achievement.AchievementCategory.FRIENDS;
                break;
            case FIREWORK_ROCKET:
                category = Achievement.AchievementCategory.PARTY;
                break;
            case CLOCK:
                category = Achievement.AchievementCategory.TIME;
                break;
            case ROSE_BUSH:
                category = Achievement.AchievementCategory.LOYALTY;
                break;
            case EXPERIENCE_BOTTLE:
                category = Achievement.AchievementCategory.EXPERIENCE;
                break;
            case BEACON:
                category = Achievement.AchievementCategory.LOBBY;
                break;
            case MINECART:
                GameAchievements listing = new GameAchievements(player, target, targetStatistics, item);
                listing.open(player);
                return;
            default:
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 100, 0);
                return;
        }

        AchievementListing listing = new AchievementListing(player, targetStatistics, target, category, item);
        listing.open(player);
    }
}
