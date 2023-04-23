/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.gui.stats.stats;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.permissions.PlusSubscription;
import net.auroramc.api.stats.Achievement;
import net.auroramc.api.stats.PlayerStatistics;
import net.auroramc.api.utils.PlayerKitLevel;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.core.gui.stats.achievements.game.GameAchievementListing;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class RunStatistics extends GUI {

    private final AuroraMCServerPlayer player;
    private final String name;
    private final PlayerStatistics stats;
    private final PlusSubscription subscription;
    private final int playerId;

    public RunStatistics(AuroraMCServerPlayer player, String targetName, PlayerStatistics targetStatistics, PlusSubscription subscription, int playerId) {
        super(String.format("&b&l%s's Run Statistics", targetName), 5, true);

        this.player = player;
        this.name = targetName;
        this.stats = targetStatistics;
        this.subscription = subscription;
        this.playerId = playerId;

        border(String.format("&b&l%s's Statistics", name), "");
        this.setItem(0, 4, new GUIItem(Material.SKULL_ITEM, String.format("&b&l%s's Statistics", name), 1, "", (short) 3, false, name));
        this.setItem(0, 0, new GUIItem(Material.ARROW, "&3&lBACK", 1, "&r&fReturn to the statistics menu"));

        //double kdr = targetStatistics.getStatistic(104, "tags") / (double)((targetStatistics.getStatistic(104, "tagged") == 0)?1:targetStatistics.getStatistic(104, "tagged"));
        //        double finalKdr = (Math.round(kdr * 100))/100.0;
        //        this.setItem(2, 2, new GUIItem(Material.STAINED_CLAY, "&b&lGame Statistics", 1, ";&r&fPeople Tagged: **" + targetStatistics.getStatistic(104, "tags") + "**;&r&fGot Tagged: **" + targetStatistics.getStatistic(104, "tagged") + "**;&r&fTag/Tagged Ratio: **" + finalKdr + "**", (short)14));
        this.setItem(2, 2, new GUIItem(Material.STAINED_CLAY, "&b&lGame Statistics", 1, ";&r&fBlocks Broken: **" + targetStatistics.getStatistic(104, "blocksBroken") + "**", (short)14));

        long losses = (targetStatistics.getStatistic(104, "gamesPlayed") - targetStatistics.getStatistic(104, "gamesWon"));
        double wlr = ((losses == 0)?targetStatistics.getStatistic(104, "gamesWon"):(double)targetStatistics.getStatistic(104, "gamesWon")/losses);
        double finalWlr = (Math.round(wlr * 100))/100.0;
        this.setItem(2, 4, new GUIItem(Material.STAINED_CLAY, "&b&lGame Statistics", 1, ";&r&fGames Played: **" + targetStatistics.getStatistic(104, "gamesPlayed") + "**;&r&fWins: **" + targetStatistics.getStatistic(104, "gamesWon") + "**;&r&fLosses: **" + losses + "**;&r&fWin/Loss Ratio: **" + finalWlr + "**;;&r&fCrowns Earned: **" + String.format("%,d", targetStatistics.getStatistic(104, "crownsEarned"))  + "**;&r&fTickets Earned: **" + String.format("%,d", targetStatistics.getStatistic(104, "ticketsEarned")) + "**;&r&fExperience Earned: **" + String.format("%,d", targetStatistics.getStatistic(104, "xpEarned")) + "**", (short)14));
        PlayerKitLevel hotPotatoLevel = AuroraMCAPI.getDbManager().getKitLevel(playerId, 104, 0);
        this.setItem(2, 6, new GUIItem(Material.STAINED_CLAY, "&b&lKit Statistics", 1, ";&3&lBerserker;&r&fLevel: **" + hotPotatoLevel.getLevel() + "**;&r&fTotal XP: **" + String.format("%,d", hotPotatoLevel.getTotalXpEarned()) + "**;&r&fCurrent Upgrade: **" + hotPotatoLevel.getLatestUpgrade() + "**", (short)14));

        long totalGameAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 104).filter((Achievement::isVisible)).count() + player.getStats().getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 104 && !achievement.isVisible()).count();

        this.setItem(3, 4, new GUIItem(Material.BOOK, "&bRun Achievements", 1, String.format("&r&fAchieved: **%s**;&r&fTotal Achievements: **%s**;;&aClick to view more!", player.getStats().getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 104).count(), totalGameAchievements)));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (item.getType() == Material.ARROW) {
            Stats stats = new Stats(player, name, this.stats, this.subscription, playerId);
            stats.open(player);
        } else if (item.getType() == Material.BOOK) {
            GameAchievementListing gameAchievementListing = new GameAchievementListing(player, stats, name, item, 104, "Run");
            gameAchievementListing.open(player);
        } else {
            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
        }
    }
}
