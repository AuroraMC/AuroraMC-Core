/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.core.gui.stats.stats;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.permissions.PlusSubscription;
import net.auroramc.api.stats.PlayerStatistics;
import net.auroramc.api.utils.PlayerKitLevel;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.core.gui.stats.achievements.game.GameAchievementListing;
import net.auroramc.api.stats.Achievement;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class SpleefStatistics extends GUI {

    private final AuroraMCServerPlayer player;
    private final String name;
    private final PlayerStatistics stats;
    private final PlusSubscription subscription;
    private final int playerId;

    public SpleefStatistics(AuroraMCServerPlayer player, String targetName, PlayerStatistics targetStatistics, PlusSubscription subscription, int playerId) {
        super(String.format("&b&l%s's Spleef Statistics", targetName), 5, true);

        this.player = player;
        this.name = targetName;
        this.stats = targetStatistics;
        this.subscription = subscription;
        this.playerId = playerId;

        border(String.format("&b&l%s's Statistics", name), "");
        this.setItem(0, 4, new GUIItem(Material.SKULL_ITEM, String.format("&b&l%s's Statistics", name), 1, "", (short) 3, false, name));
        this.setItem(0, 0, new GUIItem(Material.ARROW, "&3&lBACK", 1, "&r&fReturn to the statistics menu"));

        this.setItem(2, 2, new GUIItem(Material.IRON_SPADE, "&b&lGame Statistics", 1, ";&r&fBlocks Broken: **" + targetStatistics.getStatistic(100, "blocksBroken") + "**;&r&fSnowballs Thrown: **" + targetStatistics.getStatistic(100, "snowballsThrown") + "**"));
        long losses = (targetStatistics.getStatistic(100, "gamesPlayed") - targetStatistics.getStatistic(100, "gamesWon"));
        double wlr = ((losses == 0)?targetStatistics.getStatistic(100, "gamesWon"):(double)targetStatistics.getStatistic(100, "gamesWon")/losses);
        double finalWlr = (Math.round(wlr * 100))/100.0;
        this.setItem(2, 4, new GUIItem(Material.IRON_SPADE, "&b&lGame Statistics", 1, ";&r&fGames Played: **" + targetStatistics.getStatistic(100, "gamesPlayed") + "**;&r&fWins: **" + targetStatistics.getStatistic(100, "gamesWon") + "**;&r&fLosses: **" + losses + "**;&r&fWin/Loss Ratio: **" + finalWlr + "**;;&r&fCrowns Earned: **" + String.format("%,d", targetStatistics.getStatistic(100, "crownsEarned"))  + "**;&r&fTickets Earned: **" + String.format("%,d", targetStatistics.getStatistic(100, "ticketsEarned")) + "**;&r&fExperience Earned: **" + String.format("%,d", targetStatistics.getStatistic(100, "xpEarned")) + "**"));
        PlayerKitLevel spleefLevel = AuroraMCAPI.getDbManager().getKitLevel(playerId, 100, 0);
        this.setItem(2, 6, new GUIItem(Material.IRON_SPADE, "&b&lKit Statistics", 1, ";&3&lSnowman Kit;&r&fLevel: **" + spleefLevel.getLevel() + "**;&r&fTotal XP: **" + String.format("%,d", spleefLevel.getTotalXpEarned()) + "**;&r&fCurrent Upgrade: **" + spleefLevel.getLatestUpgrade() + "**"));

        long totalGameAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 100).filter((Achievement::isVisible)).count() + player.getStats().getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 100 && !achievement.isVisible()).count();

        this.setItem(3, 4, new GUIItem(Material.BOOK, "&bSpleef Achievements", 1, String.format("&r&fAchieved: **%s**;&r&fTotal Achievements: **%s**;;&aClick to view more!", player.getStats().getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 100).count(), totalGameAchievements)));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (item.getType() == Material.ARROW) {
            Stats stats = new Stats(player, name, this.stats, this.subscription, playerId);
            stats.open(player);
        } else if (item.getType() == Material.BOOK) {
            GameAchievementListing gameAchievementListing = new GameAchievementListing(player, stats, name, item, 100, "Spleef");
            gameAchievementListing.open(player);
        } else {
            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
        }
    }
}
