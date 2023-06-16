/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.gui.stats.stats;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.permissions.PlusSubscription;
import net.auroramc.api.stats.Achievement;
import net.auroramc.api.stats.PlayerStatistics;
import net.auroramc.api.utils.PlayerKitLevel;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import net.auroramc.smp.api.utils.gui.GUI;
import net.auroramc.smp.api.utils.gui.GUIItem;
import net.auroramc.smp.gui.stats.achievements.game.GameAchievementListing;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class CrystalQuestStatistics extends GUI {

    private final AuroraMCServerPlayer player;
    private final String name;
    private final PlayerStatistics stats;
    private final PlusSubscription subscription;
    private final int playerId;

    public CrystalQuestStatistics(AuroraMCServerPlayer player, String targetName, PlayerStatistics targetStatistics, PlusSubscription subscription, int playerId) {
        super(String.format("&b&l%s's Crystal Quest Statistics", targetName), 5, true);

        this.player = player;
        this.name = targetName;
        this.stats = targetStatistics;
        this.subscription = subscription;
        this.playerId = playerId;

        border(String.format("&b&l%s's Statistics", name), "");
        this.setItem(0, 4, new GUIItem(Material.PLAYER_HEAD, String.format("&b&l%s's Statistics", name), 1, "", (short) 0, false, name));
        this.setItem(0, 0, new GUIItem(Material.ARROW, "&3&lBACK", 1, "&r&fReturn to the statistics menu"));

        this.setItem(2, 2, new GUIItem(Material.NETHER_STAR, "&b&lCrystal Statistics", 1, ";&r&fCrystals Collected: **" + targetStatistics.getStatistic(1, "crystalsCollected") + "**;&r&fCrystals Captured: **" + targetStatistics.getStatistic(1, "crystalsCaptured") + "**;&r&fCrystal Captures Failed: **" + (targetStatistics.getStatistic(1, "crystalsCollected") - targetStatistics.getStatistic(1, "crystalsCaptured")) + "**"));
        long losses = (targetStatistics.getStatistic(1, "gamesPlayed") - targetStatistics.getStatistic(1, "gamesWon"));
        double wlr = ((losses == 0)?targetStatistics.getStatistic(1, "gamesWon"):(double)targetStatistics.getStatistic(1, "gamesWon")/losses);
        double finalWlr = (Math.round(wlr * 100))/100.0;
        double kdr = ((targetStatistics.getStatistic(1, "deaths") == 0)?targetStatistics.getStatistic(1, "kills"):(double)targetStatistics.getStatistic(1, "kills") / targetStatistics.getStatistic(1, "deaths"));
        double finalKdr = (Math.round(kdr * 100))/100.0;
        this.setItem(2, 4, new GUIItem(Material.NETHER_STAR, "&b&lGame Statistics", 1, ";&r&fGames Played: **" + targetStatistics.getStatistic(1, "gamesPlayed") + "**;&r&fWins: **" + targetStatistics.getStatistic(1, "gamesWon") + "**;&r&fLosses: **" + losses + "**;&r&fWin/Loss Ratio: **" + finalWlr + "**;;&r&fCrowns Earned: **" + String.format("%,d", targetStatistics.getStatistic(1, "crownsEarned"))  + "**;&r&fTickets Earned: **" + String.format("%,d", targetStatistics.getStatistic(1, "ticketsEarned")) + "**;&r&fExperience Earned: **" + String.format("%,d", targetStatistics.getStatistic(1, "xpEarned")) + "**;;&r&fKills: **" + targetStatistics.getStatistic(1, "kills") + "**;&r&fDeaths: **" + targetStatistics.getStatistic(1, "deaths") + "**;&r&fKill/Death Ratio: **" + finalKdr + "**"));
        PlayerKitLevel minerLevel = AuroraMCAPI.getDbManager().getKitLevel(playerId, 1, 0);
        PlayerKitLevel defenderLevel = AuroraMCAPI.getDbManager().getKitLevel(playerId, 1, 1);
        PlayerKitLevel fighterLevel = AuroraMCAPI.getDbManager().getKitLevel(playerId, 1, 2);
        PlayerKitLevel economistLevel = AuroraMCAPI.getDbManager().getKitLevel(playerId, 1, 4);
        PlayerKitLevel archerLevel = AuroraMCAPI.getDbManager().getKitLevel(playerId, 1, 3);
        this.setItem(2, 6, new GUIItem(Material.NETHER_STAR, "&b&lKit Statistics", 1, ";&3&lMiner Kit;&r&fLevel: **" + minerLevel.getLevel() + "**;&r&fTotal XP: **" + String.format("%,d", minerLevel.getTotalXpEarned()) + "**;&r&fCurrent Upgrade: **" + minerLevel.getLatestUpgrade() + "**;;&3&lDefender Kit;&r&fLevel: **" + defenderLevel.getLevel() + "**;&r&fTotal XP: **" + String.format("%,d", defenderLevel.getTotalXpEarned()) + "**;&r&fCurrent Upgrade: **" + minerLevel.getLatestUpgrade() + "**;;&3&lFighter Kit;&r&fLevel: **" + fighterLevel.getLevel() + "**;&r&fTotal XP: **" + String.format("%,d", fighterLevel.getTotalXpEarned()) + "**;&r&fCurrent Upgrade: **" + fighterLevel.getLatestUpgrade() + "**;;&3&lArcher Kit;&r&fLevel: **" + archerLevel.getLevel() + "**;&r&fTotal XP: **" + String.format("%,d", archerLevel.getTotalXpEarned()) + "**;&r&fCurrent Upgrade: **" + archerLevel.getLatestUpgrade() + "**;;&3&lEconomist Kit;&r&fLevel: **" + economistLevel.getLevel() + "**;&r&fTotal XP: **" + String.format("%,d", economistLevel.getTotalXpEarned()) + "**;&r&fCurrent Upgrade: **" + economistLevel.getLatestUpgrade() + "**"));

        long totalGameAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 1).filter((Achievement::isVisible)).count() + player.getStats().getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 1 && !achievement.isVisible()).count();

        this.setItem(3, 4, new GUIItem(Material.BOOK, "&bCrystal Quest Achievements", 1, String.format("&r&fAchieved: **%s**;&r&fTotal Achievements: **%s**;;&aClick to view more!", player.getStats().getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 1).count(), totalGameAchievements)));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (item.getType() == Material.ARROW) {
            Stats stats = new Stats(player, name, this.stats, this.subscription, playerId);
            stats.open(player);
        } else if (item.getType() == Material.BOOK) {
            GameAchievementListing gameAchievementListing = new GameAchievementListing(player, stats, name, item, 1, "Crystal Quest");
            gameAchievementListing.open(player);
        } else {
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 100, 0);
        }
    }
}
