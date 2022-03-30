/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.gui.stats.stats;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.PlusSubscription;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.stats.Achievement;
import net.auroramc.core.api.stats.PlayerStatistics;
import net.auroramc.core.api.utils.LevelUtils;
import net.auroramc.core.api.utils.PlayerKitLevel;
import net.auroramc.core.api.utils.TimeLength;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.core.gui.stats.achievements.game.GameAchievementListing;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;

public class CrystalQuestStatistics extends GUI {

    private final AuroraMCPlayer player;
    private final String name;
    private final PlayerStatistics stats;
    private final PlusSubscription subscription;
    private final int playerId;

    public CrystalQuestStatistics(AuroraMCPlayer player, String targetName, PlayerStatistics targetStatistics, PlusSubscription subscription, int playerId) {
        super(String.format("&b&l%s's Crystal Quest Statistics", targetName), 5, true);

        this.player = player;
        this.name = targetName;
        this.stats = targetStatistics;
        this.subscription = subscription;
        this.playerId = playerId;

        border(String.format("&b&l%s's Statistics", name), "");
        this.setItem(0, 4, new GUIItem(Material.SKULL_ITEM, String.format("&b&l%s's Statistics", name), 1, "", (short) 3, false, name));
        this.setItem(0, 0, new GUIItem(Material.ARROW, "&3&lBACK", 1, "&rReturn to the statistics menu"));

        this.setItem(2, 2, new GUIItem(Material.NETHER_STAR, "&b&lCrystal Statistics", 1, ";&rCrystals Collected: **" + targetStatistics.getStatistic(1, "crystalsCollected") + "**;&rCrystals Captured: **" + targetStatistics.getStatistic(1, "crystalsCaptured") + "**;&rCrystal Captures Failed: **" + (targetStatistics.getStatistic(1, "crystalsCollected") - targetStatistics.getStatistic(1, "crystalsCaptured")) + "**"));
        long losses = (targetStatistics.getStatistic(1, "gamesPlayed") - targetStatistics.getStatistic(1, "gamesWon"));
        double wlr = ((losses == 0)?targetStatistics.getStatistic(1, "gamesWon"):(double)targetStatistics.getStatistic(1, "gamesWon")/losses);
        double finalWlr = (Math.round(wlr * 10))/10.0;
        double kdr = ((targetStatistics.getStatistic(1, "deaths") == 0)?targetStatistics.getStatistic(1, "kills"):(double)targetStatistics.getStatistic(1, "kills") / targetStatistics.getStatistic(1, "deaths"));
        double finalKdr = (Math.round(kdr * 10))/10.0;
        this.setItem(2, 4, new GUIItem(Material.NETHER_STAR, "&b&lGame Statistics", 1, ";&rGames Played: **" + targetStatistics.getStatistic(1, "gamesPlayed") + "**;&rWins: **" + targetStatistics.getStatistic(1, "gamesWon") + "**;&rLosses: **" + losses + "**;&rWin/Loss Ratio: **" + finalWlr + "**;;&rCrowns Earned: **" + targetStatistics.getStatistic(1, "crownsEarned") + "**;&rTickets Earned: **" + targetStatistics.getStatistic(1, "ticketsEarned") + "**;&rExperience Earned: **" + targetStatistics.getStatistic(1, "xpEarned") + "**;;&rKills: **" + targetStatistics.getStatistic(1, "kills") + "**;&rDeaths: **" + targetStatistics.getStatistic(1, "deaths") + "**;&rKill/Death Ratio: **" + finalKdr + "**"));
        PlayerKitLevel minerLevel = AuroraMCAPI.getDbManager().getKitLevel(playerId, 1, 0);
        PlayerKitLevel defenderLevel = AuroraMCAPI.getDbManager().getKitLevel(playerId, 1, 1);
        PlayerKitLevel fighterLevel = AuroraMCAPI.getDbManager().getKitLevel(playerId, 1, 2);
        this.setItem(2, 6, new GUIItem(Material.NETHER_STAR, "&b&lKit Statistics", 1, ";&3&lMiner Kit;&rLevel: **" + minerLevel.getLevel() + "**;&rTotal XP: **" + minerLevel.getTotalXpEarned() + "**;&rCurrent Upgrade: **" + minerLevel.getLatestUpgrade() + "**;;&3&lDefender Kit;&rLevel: **" + defenderLevel.getLevel() + "**;&rTotal XP: **" + defenderLevel.getTotalXpEarned() + "**;&rCurrent Upgrade: **" + minerLevel.getLatestUpgrade() + "**;;&3&lFighter Kit;&rLevel: **" + fighterLevel.getLevel() + "**;&rTotal XP: **" + fighterLevel.getTotalXpEarned() + "**;&rCurrent Upgrade: **" + fighterLevel.getLatestUpgrade() + "**"));

        long totalGameAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME).filter((Achievement::isVisible)).count() + player.getStats().getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 1 && !achievement.isVisible()).count();

        this.setItem(3, 4, new GUIItem(Material.BOOK, "&bCrystal Quest Achievements", 1, String.format("&rAchieved: **%s**;&rTotal Achievements: **%s**;;&aClick to view more!", player.getStats().getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 1).count(), totalGameAchievements)));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (item.getType() == Material.ARROW) {
            AuroraMCAPI.closeGUI(player);
            Stats stats = new Stats(player, name, this.stats, this.subscription, playerId);
            stats.open(player);
            AuroraMCAPI.openGUI(player, stats);
        } else if (item.getType() == Material.BOOK) {
            GameAchievementListing gameAchievementListing = new GameAchievementListing(player, stats, name, item, 1, "Crystal Quest");
            AuroraMCAPI.closeGUI(player);
            gameAchievementListing.open(player);
            AuroraMCAPI.openGUI(player, gameAchievementListing);
        } else {
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
        }
    }
}
