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

public class TagStatistics extends GUI {

    private final AuroraMCPlayer player;
    private final String name;
    private final PlayerStatistics stats;
    private final PlusSubscription subscription;
    private final int playerId;

    public TagStatistics(AuroraMCPlayer player, String targetName, PlayerStatistics targetStatistics, PlusSubscription subscription, int playerId) {
        super(String.format("&b&l%s's Tag Statistics", targetName), 5, true);

        this.player = player;
        this.name = targetName;
        this.stats = targetStatistics;
        this.subscription = subscription;
        this.playerId = playerId;

        border(String.format("&b&l%s's Statistics", name), "");
        this.setItem(0, 4, new GUIItem(Material.SKULL_ITEM, String.format("&b&l%s's Statistics", name), 1, "", (short) 3, false, name));
        this.setItem(0, 0, new GUIItem(Material.ARROW, "&3&lBACK", 1, "&rReturn to the statistics menu"));

        double kdr = targetStatistics.getStatistic(103, "tags") / (double)((targetStatistics.getStatistic(103, "tagged") == 0)?1:targetStatistics.getStatistic(103, "tagged"));
        double finalKdr = (Math.round(kdr * 100))/100.0;
        this.setItem(2, 2, new GUIItem(Material.LEASH, "&b&lGame Statistics", 1, ";&rPeople Tagged: **" + targetStatistics.getStatistic(103, "tags") + "**;&rGot Tagged: **" + targetStatistics.getStatistic(103, "tagged") + "**;&rTag/Tagged Ratio: **" + finalKdr + "**"));

        long losses = (targetStatistics.getStatistic(103, "gamesPlayed") - targetStatistics.getStatistic(103, "gamesWon"));
        double wlr = ((losses == 0)?targetStatistics.getStatistic(103, "gamesWon"):(double)targetStatistics.getStatistic(103, "gamesWon")/losses);
        double finalWlr = (Math.round(wlr * 100))/100.0;
        this.setItem(2, 4, new GUIItem(Material.LEASH, "&b&lGame Statistics", 1, ";&rGames Played: **" + targetStatistics.getStatistic(103, "gamesPlayed") + "**;&rWins: **" + targetStatistics.getStatistic(103, "gamesWon") + "**;&rLosses: **" + losses + "**;&rWin/Loss Ratio: **" + finalWlr + "**;;&rCrowns Earned: **" + String.format("%,d", targetStatistics.getStatistic(103, "crownsEarned"))  + "**;&rTickets Earned: **" + String.format("%,d", targetStatistics.getStatistic(103, "ticketsEarned")) + "**;&rExperience Earned: **" + String.format("%,d", targetStatistics.getStatistic(103, "xpEarned")) + "**"));
        PlayerKitLevel hotPotatoLevel = AuroraMCAPI.getDbManager().getKitLevel(playerId, 103, 0);
        this.setItem(2, 6, new GUIItem(Material.LEASH, "&b&lKit Statistics", 1, ";&3&lTag Kit;&rLevel: **" + hotPotatoLevel.getLevel() + "**;&rTotal XP: **" + String.format("%,d", hotPotatoLevel.getTotalXpEarned()) + "**;&rCurrent Upgrade: **" + hotPotatoLevel.getLatestUpgrade() + "**"));

        long totalGameAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 103).filter((Achievement::isVisible)).count() + player.getStats().getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 103 && !achievement.isVisible()).count();

        this.setItem(3, 4, new GUIItem(Material.BOOK, "&bTag Achievements", 1, String.format("&rAchieved: **%s**;&rTotal Achievements: **%s**;;&aClick to view more!", player.getStats().getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 103).count(), totalGameAchievements)));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (item.getType() == Material.ARROW) {
            AuroraMCAPI.closeGUI(player);
            Stats stats = new Stats(player, name, this.stats, this.subscription, playerId);
            stats.open(player);
            AuroraMCAPI.openGUI(player, stats);
        } else if (item.getType() == Material.BOOK) {
            GameAchievementListing gameAchievementListing = new GameAchievementListing(player, stats, name, item, 103, "Tag");
            AuroraMCAPI.closeGUI(player);
            gameAchievementListing.open(player);
            AuroraMCAPI.openGUI(player, gameAchievementListing);
        } else {
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
        }
    }
}