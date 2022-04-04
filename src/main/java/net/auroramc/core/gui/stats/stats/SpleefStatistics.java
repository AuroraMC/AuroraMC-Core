/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.gui.stats.stats;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.PlusSubscription;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.stats.Achievement;
import net.auroramc.core.api.stats.PlayerStatistics;
import net.auroramc.core.api.utils.PlayerKitLevel;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.core.gui.stats.achievements.game.GameAchievementListing;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class SpleefStatistics extends GUI {

    private final AuroraMCPlayer player;
    private final String name;
    private final PlayerStatistics stats;
    private final PlusSubscription subscription;
    private final int playerId;

    public SpleefStatistics(AuroraMCPlayer player, String targetName, PlayerStatistics targetStatistics, PlusSubscription subscription, int playerId) {
        super(String.format("&b&l%s's Spleef Statistics", targetName), 5, true);

        this.player = player;
        this.name = targetName;
        this.stats = targetStatistics;
        this.subscription = subscription;
        this.playerId = playerId;

        border(String.format("&b&l%s's Statistics", name), "");
        this.setItem(0, 4, new GUIItem(Material.SKULL_ITEM, String.format("&b&l%s's Statistics", name), 1, "", (short) 3, false, name));
        this.setItem(0, 0, new GUIItem(Material.ARROW, "&3&lBACK", 1, "&rReturn to the statistics menu"));

        this.setItem(2, 2, new GUIItem(Material.SNOW_BALL, "&b&lGame Statistics", 1, ";&rBlocks Broken: **" + targetStatistics.getStatistic(101, "blocksBroken") + "**"));
        long losses = (targetStatistics.getStatistic(101, "gamesPlayed") - targetStatistics.getStatistic(101, "gamesWon"));
        double wlr = ((losses == 0)?targetStatistics.getStatistic(101, "gamesWon"):(double)targetStatistics.getStatistic(101, "gamesWon")/losses);
        double finalWlr = (Math.round(wlr * 100))/100.0;
        this.setItem(2, 4, new GUIItem(Material.SNOW_BALL, "&b&lGame Statistics", 1, ";&rGames Played: **" + targetStatistics.getStatistic(101, "gamesPlayed") + "**;&rWins: **" + targetStatistics.getStatistic(101, "gamesWon") + "**;&rLosses: **" + losses + "**;&rWin/Loss Ratio: **" + finalWlr + "**;;&rCrowns Earned: **" + String.format("%,d", targetStatistics.getStatistic(101, "crownsEarned"))  + "**;&rTickets Earned: **" + String.format("%,d", targetStatistics.getStatistic(1, "ticketsEarned")) + "**;&rExperience Earned: **" + String.format("%,d", targetStatistics.getStatistic(101, "xpEarned")) + "**"));
        PlayerKitLevel spleefLevel = AuroraMCAPI.getDbManager().getKitLevel(playerId, 101, 0);
        this.setItem(2, 6, new GUIItem(Material.SNOW_BALL, "&b&lKit Statistics", 1, ";&3&lSpleef Kit;&rLevel: **" + spleefLevel.getLevel() + "**;&rTotal XP: **" + String.format("%,d", spleefLevel.getTotalXpEarned()) + "**;&rCurrent Upgrade: **" + spleefLevel.getLatestUpgrade() + "**"));

        long totalGameAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 101).filter((Achievement::isVisible)).count() + player.getStats().getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 101 && !achievement.isVisible()).count();

        this.setItem(3, 4, new GUIItem(Material.BOOK, "&bCrystal Quest Achievements", 1, String.format("&rAchieved: **%s**;&rTotal Achievements: **%s**;;&aClick to view more!", player.getStats().getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 101).count(), totalGameAchievements)));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (item.getType() == Material.ARROW) {
            AuroraMCAPI.closeGUI(player);
            Stats stats = new Stats(player, name, this.stats, this.subscription, playerId);
            stats.open(player);
            AuroraMCAPI.openGUI(player, stats);
        } else if (item.getType() == Material.BOOK) {
            GameAchievementListing gameAchievementListing = new GameAchievementListing(player, stats, name, item, 101, "Spleef");
            AuroraMCAPI.closeGUI(player);
            gameAchievementListing.open(player);
            AuroraMCAPI.openGUI(player, gameAchievementListing);
        } else {
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
        }
    }
}
