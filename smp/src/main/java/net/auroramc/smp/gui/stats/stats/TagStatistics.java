/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
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

public class TagStatistics extends GUI {

    private final AuroraMCServerPlayer player;
    private final String name;
    private final PlayerStatistics stats;
    private final PlusSubscription subscription;
    private final int playerId;

    public TagStatistics(AuroraMCServerPlayer player, String targetName, PlayerStatistics targetStatistics, PlusSubscription subscription, int playerId) {
        super(String.format("&b&l%s's Tag Statistics", targetName), 5, true);

        this.player = player;
        this.name = targetName;
        this.stats = targetStatistics;
        this.subscription = subscription;
        this.playerId = playerId;

        border(String.format("&b&l%s's Statistics", name), "");
        this.setItem(0, 4, new GUIItem(Material.PLAYER_HEAD, String.format("&b&l%s's Statistics", name), 1, "", (short) 0, false, name));
        this.setItem(0, 0, new GUIItem(Material.ARROW, "&3&lBACK", 1, "&r&fReturn to the statistics menu"));

        double kdr = targetStatistics.getStatistic(103, "tags") / (double)((targetStatistics.getStatistic(103, "tagged") == 0)?1:targetStatistics.getStatistic(103, "tagged"));
        double finalKdr = (Math.round(kdr * 100))/100.0;
        this.setItem(2, 2, new GUIItem(Material.LEAD, "&b&lGame Statistics", 1, ";&r&fPeople Tagged: **" + targetStatistics.getStatistic(103, "tags") + "**;&r&fGot Tagged: **" + targetStatistics.getStatistic(103, "tagged") + "**;&r&fTag/Tagged Ratio: **" + finalKdr + "**"));

        long losses = (targetStatistics.getStatistic(103, "gamesPlayed") - targetStatistics.getStatistic(103, "gamesWon"));
        double wlr = ((losses == 0)?targetStatistics.getStatistic(103, "gamesWon"):(double)targetStatistics.getStatistic(103, "gamesWon")/losses);
        double finalWlr = (Math.round(wlr * 100))/100.0;
        this.setItem(2, 4, new GUIItem(Material.LEAD, "&b&lGame Statistics", 1, ";&r&fGames Played: **" + targetStatistics.getStatistic(103, "gamesPlayed") + "**;&r&fWins: **" + targetStatistics.getStatistic(103, "gamesWon") + "**;&r&fLosses: **" + losses + "**;&r&fWin/Loss Ratio: **" + finalWlr + "**;;&r&fCrowns Earned: **" + String.format("%,d", targetStatistics.getStatistic(103, "crownsEarned"))  + "**;&r&fTickets Earned: **" + String.format("%,d", targetStatistics.getStatistic(103, "ticketsEarned")) + "**;&r&fExperience Earned: **" + String.format("%,d", targetStatistics.getStatistic(103, "xpEarned")) + "**"));
        PlayerKitLevel hotPotatoLevel = AuroraMCAPI.getDbManager().getKitLevel(playerId, 103, 0);
        PlayerKitLevel blinkerLevel = AuroraMCAPI.getDbManager().getKitLevel(playerId, 103, 1);
        this.setItem(2, 6, new GUIItem(Material.LEAD, "&b&lKit Statistics", 1, ";&3&lPlayer Kit;&r&fLevel: **" + hotPotatoLevel.getLevel() + "**;&r&fTotal XP: **" + String.format("%,d", hotPotatoLevel.getTotalXpEarned()) + "**;&r&fCurrent Upgrade: **" + hotPotatoLevel.getLatestUpgrade() + "**;;&3&lBlinker Kit;&r&fLevel: **" + blinkerLevel.getLevel() + "**;&r&fTotal XP: **" + String.format("%,d", blinkerLevel.getTotalXpEarned()) + "**;&r&fCurrent Upgrade: **" + blinkerLevel.getLatestUpgrade() + "**"));

        long totalGameAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 103).filter((Achievement::isVisible)).count() + player.getStats().getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 103 && !achievement.isVisible()).count();

        this.setItem(3, 4, new GUIItem(Material.BOOK, "&bTag Achievements", 1, String.format("&r&fAchieved: **%s**;&r&fTotal Achievements: **%s**;;&aClick to view more!", player.getStats().getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 103).count(), totalGameAchievements)));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (item.getType() == Material.ARROW) {
            Stats stats = new Stats(player, name, this.stats, this.subscription, playerId);
            stats.open(player);
        } else if (item.getType() == Material.BOOK) {
            GameAchievementListing gameAchievementListing = new GameAchievementListing(player, stats, name, item, 103, "Tag");
            gameAchievementListing.open(player);
        } else {
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 100, 0);
        }
    }
}
