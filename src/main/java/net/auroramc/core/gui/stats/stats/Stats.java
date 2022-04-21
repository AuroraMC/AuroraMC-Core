/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.gui.stats.stats;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.PlusSubscription;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.stats.Achievement;
import net.auroramc.core.api.utils.TimeLength;
import net.auroramc.core.api.stats.PlayerStatistics;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.core.gui.stats.achievements.Achievements;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class Stats extends GUI {

    private final AuroraMCPlayer player;
    private final String name;
    private final PlayerStatistics stats;
    private final PlusSubscription subscription;
    private final int playerId;

    public Stats(AuroraMCPlayer player, String targetName, PlayerStatistics targetStatistics, PlusSubscription subscription, int playerId) {
        super("&3&l" + targetName + "'s Statistics", 5, true);

        this.player = player;
        this.name = targetName;
        this.stats = targetStatistics;
        this.subscription = subscription;
        this.playerId = playerId;

        border(String.format("&3&l%s's Statistics", name), "");
        this.setItem(0, 4, new GUIItem(Material.SKULL_ITEM, String.format("&3&l%s's Statistics", name), 1, "", (short) 3, false, name));
        TimeLength total = new TimeLength((stats.getGameTimeMs() + stats.getLobbyTimeMs())/3600000d, false);
        this.setItem(0, 2, new GUIItem(Material.BOOK, "&a&lGeneral Statistics", 1, ";&rIn-Game Time: **" + total.getFormatted() + "**;&rLevel: **" + targetStatistics.getLevel() + "**;&rTotal Experience Earned: **" + String.format("%,d", stats.getTotalXpEarned()) + "**;;&aClick here to view more!"));
        long totalAchievements = AuroraMCAPI.getAchievements().values().stream().filter((Achievement::isVisible)).count() + targetStatistics.getAchievementsGained().keySet().stream().filter((achievement -> !achievement.isVisible())).count();
        this.setItem(0, 6, new GUIItem(Material.FIREWORK, "&3&lView Achievements", 1, String.format(";&rAchieved: **%s**;&rTotal Achievements: **%s**;;&aClick to view more!", targetStatistics.getAchievementsGained().size(), totalAchievements)));

        this.setItem(2, 2, new GUIItem(Material.NETHER_STAR, "&b&lCrystal Quest", 1, ";&rGames Played: **" + targetStatistics.getStatistic(1, "gamesPlayed") + "**;&rWins: **" + targetStatistics.getStatistic(1, "gamesWon") + "**;&rLosses: **" + (targetStatistics.getStatistic(1, "gamesPlayed") - targetStatistics.getStatistic(1, "gamesWon")) + "**;;&aClick to view more!"));
        this.setItem(2, 3, new GUIItem(Material.IRON_SWORD, "&c&lBackstab", 1, ";&rGames Played: **" + targetStatistics.getStatistic(2, "gamesPlayed") + "**;&rWins: **" + targetStatistics.getStatistic(2, "gamesWon") + "**;&rLosses: **" + (targetStatistics.getStatistic(2, "gamesPlayed") - targetStatistics.getStatistic(2, "gamesWon")) + "**;;&aClick to view more!"));
        this.setItem(2, 4, new GUIItem(Material.SNOW_BALL, "&a&lPaintball", 1, ";&rGames Played: **" + targetStatistics.getStatistic(3, "gamesPlayed") + "**;&rWins: **" + targetStatistics.getStatistic(3, "gamesWon") + "**;&rLosses: **" + (targetStatistics.getStatistic(3, "gamesPlayed") - targetStatistics.getStatistic(3, "gamesWon")) + "**;;&aClick to view more!"));
        this.setItem(2, 5, new GUIItem(Material.IRON_SPADE, "&b&lSpleef", 1, ";&rGames Played: **" + targetStatistics.getStatistic(100, "gamesPlayed") + "**;&rWins: **" + targetStatistics.getStatistic(100, "gamesWon") + "**;&rLosses: **" + (targetStatistics.getStatistic(100, "gamesPlayed") - targetStatistics.getStatistic(100, "gamesWon")) + "**;;&aClick to view more!"));
        this.setItem(2, 6, new GUIItem(Material.BAKED_POTATO, "&c&lHotPotato", 1, ";&rGames Played: **" + targetStatistics.getStatistic(101, "gamesPlayed") + "**;&rWins: **" + targetStatistics.getStatistic(101, "gamesWon") + "**;&rLosses: **" + (targetStatistics.getStatistic(101, "gamesPlayed") - targetStatistics.getStatistic(101, "gamesWon")) + "**;;&aClick to view more!"));
        this.setItem(3, 3, new GUIItem(Material.IRON_AXE, "&c&lFFA", 1, ";&rGames Played: **" + targetStatistics.getStatistic(102, "gamesPlayed") + "**;&rWins: **" + targetStatistics.getStatistic(102, "gamesWon") + "**;&rLosses: **" + (targetStatistics.getStatistic(102, "gamesPlayed") - targetStatistics.getStatistic(102, "gamesWon")) + "**;;&aClick to view more!"));
        this.setItem(3, 4, new GUIItem(Material.LEASH, "&c&lTag", 1, ";&rGames Played: **" + targetStatistics.getStatistic(103, "gamesPlayed") + "**;&rWins: **" + targetStatistics.getStatistic(103, "gamesWon") + "**;&rLosses: **" + (targetStatistics.getStatistic(103, "gamesPlayed") - targetStatistics.getStatistic(103, "gamesWon")) + "**;;&aClick to view more!"));
        this.setItem(3, 5, new GUIItem(Material.STAINED_CLAY, "&e&lRun", 1, ";&rGames Played: **" + targetStatistics.getStatistic(104, "gamesPlayed") + "**;&rWins: **" + targetStatistics.getStatistic(104, "gamesWon") + "**;&rLosses: **" + (targetStatistics.getStatistic(104, "gamesPlayed") - targetStatistics.getStatistic(104, "gamesWon")) + "**;;&aClick to view more!", (short)14));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (item.getType() == Material.STAINED_GLASS_PANE) {
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
            return;
        }
        switch (item.getType()) {
            case BOOK: {
                AuroraMCAPI.closeGUI(player);
                GeneralStatistics stats = new GeneralStatistics(player, name, this.stats, this.subscription, playerId);
                stats.open(player);
                AuroraMCAPI.openGUI(player, stats);
                break;
            }
            case FIREWORK: {
                AuroraMCAPI.closeGUI(player);
                Achievements stats = new Achievements(player, this.stats, name);
                stats.open(player);
                AuroraMCAPI.openGUI(player, stats);
                break;
            }
            case NETHER_STAR: {
                AuroraMCAPI.closeGUI(player);
                CrystalQuestStatistics stats = new CrystalQuestStatistics(player, name, this.stats, this.subscription, playerId);
                stats.open(player);
                AuroraMCAPI.openGUI(player, stats);
                break;
            }
            case IRON_SPADE: {
                AuroraMCAPI.closeGUI(player);
                SpleefStatistics stats = new SpleefStatistics(player, name, this.stats, this.subscription, playerId);
                stats.open(player);
                AuroraMCAPI.openGUI(player, stats);
                break;
            }
            case IRON_AXE: {
                AuroraMCAPI.closeGUI(player);
                FFAStatistics stats = new FFAStatistics(player, name, this.stats, this.subscription, playerId);
                stats.open(player);
                AuroraMCAPI.openGUI(player, stats);
                break;
            }
            case BAKED_POTATO: {
                AuroraMCAPI.closeGUI(player);
                HotPotatoStatistics stats = new HotPotatoStatistics(player, name, this.stats, this.subscription, playerId);
                stats.open(player);
                AuroraMCAPI.openGUI(player, stats);
                break;
            }
            case STAINED_CLAY: {
                AuroraMCAPI.closeGUI(player);
                RunStatistics stats = new RunStatistics(player, name, this.stats, this.subscription, playerId);
                stats.open(player);
                AuroraMCAPI.openGUI(player, stats);
                break;
            }
            case LEASH: {
                AuroraMCAPI.closeGUI(player);
                TagStatistics stats = new TagStatistics(player, name, this.stats, this.subscription, playerId);
                stats.open(player);
                AuroraMCAPI.openGUI(player, stats);
                break;
            }
            case SNOW_BALL: {
                AuroraMCAPI.closeGUI(player);
                PaintballStatistics stats = new PaintballStatistics(player, name, this.stats, this.subscription, playerId);
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
