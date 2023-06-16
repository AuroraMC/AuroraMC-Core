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

public class PaintballStatistics extends GUI {

    private final AuroraMCServerPlayer player;
    private final String name;
    private final PlayerStatistics stats;
    private final PlusSubscription subscription;
    private final int playerId;

    public PaintballStatistics(AuroraMCServerPlayer player, String targetName, PlayerStatistics targetStatistics, PlusSubscription subscription, int playerId) {
        super(String.format("&b&l%s's Paintball Statistics", targetName), 5, true);

        this.player = player;
        this.name = targetName;
        this.stats = targetStatistics;
        this.subscription = subscription;
        this.playerId = playerId;

        border(String.format("&b&l%s's Statistics", name), "");
        this.setItem(0, 4, new GUIItem(Material.PLAYER_HEAD, String.format("&b&l%s's Statistics", name), 1, "", (short) 0, false, name));
        this.setItem(0, 0, new GUIItem(Material.ARROW, "&3&lBACK", 1, "&r&fReturn to the statistics menu"));

        this.setItem(2, 2, new GUIItem(Material.SNOWBALL, "&b&lGame Statistics", 1, "&7Your top items brought!;;&r&fExtra Lives: **" + targetStatistics.getStatistic(3, "extraLives") + "**;&r&fMore Ammo!: **" + targetStatistics.getStatistic(3, "extraAmmo") + "**;&r&fTeam Ammo!: **" + targetStatistics.getStatistic(3, "teamAmmo") + "**;&r&fFlashbang: **" + targetStatistics.getStatistic(3, "flashbang")  + "**;&r&fTurret: **" + targetStatistics.getStatistic(3, "turret")  + "**;&r&fMissile Strike: **" + targetStatistics.getStatistic(3, "missileStrike") + "**"));

        long losses = (targetStatistics.getStatistic(3, "gamesPlayed") - targetStatistics.getStatistic(3, "gamesWon"));
        double wlr = ((losses == 0)?targetStatistics.getStatistic(1, "gamesWon"):(double)targetStatistics.getStatistic(1, "gamesWon")/losses);
        double finalWlr = (Math.round(wlr * 100))/100.0;
        double kdr = ((targetStatistics.getStatistic(1, "deaths") == 0)?targetStatistics.getStatistic(3, "kills"):(double)targetStatistics.getStatistic(3, "kills") / targetStatistics.getStatistic(3, "deaths"));
        double finalKdr = (Math.round(kdr * 100))/100.0;
        this.setItem(2, 4, new GUIItem(Material.SNOWBALL, "&b&lGame Statistics", 1, ";&r&fGames Played: **" + targetStatistics.getStatistic(3, "gamesPlayed") + "**;&r&fWins: **" + targetStatistics.getStatistic(3, "gamesWon") + "**;&r&fLosses: **" + losses + "**;&r&fWin/Loss Ratio: **" + finalWlr + "**;;&r&fCrowns Earned: **" + String.format("%,d", targetStatistics.getStatistic(3, "crownsEarned"))  + "**;&r&fTickets Earned: **" + String.format("%,d", targetStatistics.getStatistic(3, "ticketsEarned")) + "**;&r&fExperience Earned: **" + String.format("%,d", targetStatistics.getStatistic(3, "xpEarned")) + "**;;&r&fKills: **" + targetStatistics.getStatistic(3, "kills") + "**;&r&fDeaths: **" + targetStatistics.getStatistic(3, "deaths") + "**;&r&fKill/Death Ratio: **" + finalKdr + "**"));
        PlayerKitLevel hotPotatoLevel = AuroraMCAPI.getDbManager().getKitLevel(playerId, 3, 0);
        this.setItem(2, 6, new GUIItem(Material.SNOWBALL, "&b&lKit Statistics", 1, ";&3&lTribute;&r&fLevel: **" + hotPotatoLevel.getLevel() + "**;&r&fTotal XP: **" + String.format("%,d", hotPotatoLevel.getTotalXpEarned()) + "**;&r&fCurrent Upgrade: **" + hotPotatoLevel.getLatestUpgrade() + "**"));

        long totalGameAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 3).filter((Achievement::isVisible)).count() + player.getStats().getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 3 && !achievement.isVisible()).count();

        this.setItem(3, 4, new GUIItem(Material.BOOK, "&bTag Achievements", 1, String.format("&r&fAchieved: **%s**;&r&fTotal Achievements: **%s**;;&aClick to view more!", player.getStats().getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == 3).count(), totalGameAchievements)));
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
