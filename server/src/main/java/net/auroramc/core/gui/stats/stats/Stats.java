/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.gui.stats.stats;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.permissions.PlusSubscription;
import net.auroramc.api.stats.Achievement;
import net.auroramc.api.stats.PlayerStatistics;
import net.auroramc.api.utils.TimeLength;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.core.gui.stats.achievements.Achievements;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class Stats extends GUI {

    private final AuroraMCServerPlayer player;
    private final String name;
    private final PlayerStatistics stats;
    private final PlusSubscription subscription;
    private final int playerId;

    public Stats(AuroraMCServerPlayer player, String targetName, PlayerStatistics targetStatistics, PlusSubscription subscription, int playerId) {
        super("&3&l" + targetName + "'s Statistics", 5, true);

        this.player = player;
        this.name = targetName;
        this.stats = targetStatistics;
        this.subscription = subscription;
        this.playerId = playerId;

        border(String.format("&3&l%s's Statistics", name), "");
        this.setItem(0, 4, new GUIItem(Material.SKULL_ITEM, String.format("&3&l%s's Statistics", name), 1, "", (short) 3, false, name));
        TimeLength total = new TimeLength((stats.getGameTimeMs() + stats.getLobbyTimeMs())/3600000d, false);
        this.setItem(0, 2, new GUIItem(Material.BOOK, "&a&lGeneral Statistics", 1, ";&r&fIn-Game Time: **" + total.getFormatted() + "**;&r&fLevel: **" + targetStatistics.getLevel() + "**;&r&fTotal Experience Earned: **" + String.format("%,d", stats.getTotalXpEarned()) + "**;;&aClick here to view more!"));
        long totalAchievements = AuroraMCAPI.getAchievements().values().stream().filter((Achievement::isVisible)).count() + targetStatistics.getAchievementsGained().keySet().stream().filter((achievement -> !achievement.isVisible())).count();
        this.setItem(0, 6, new GUIItem(Material.FIREWORK, "&3&lView Achievements", 1, String.format(";&r&fAchieved: **%s**;&r&fTotal Achievements: **%s**;;&aClick to view more!", targetStatistics.getAchievementsGained().size(), totalAchievements)));

        this.setItem(2, 2, new GUIItem(Material.NETHER_STAR, "&b&lCrystal Quest", 1, ";&r&fGames Played: **" + targetStatistics.getStatistic(1, "gamesPlayed") + "**;&r&fWins: **" + targetStatistics.getStatistic(1, "gamesWon") + "**;&r&fLosses: **" + (targetStatistics.getStatistic(1, "gamesPlayed") - targetStatistics.getStatistic(1, "gamesWon")) + "**;;&aClick to view more!"));
        this.setItem(2, 3, new GUIItem(Material.IRON_SWORD, "&c&lDuels", 1, ";&r&fGames Played: **" + targetStatistics.getStatistic(4, "gamesPlayed") + "**;&r&fWins: **" + targetStatistics.getStatistic(4, "gamesWon") + "**;&r&fLosses: **" + (targetStatistics.getStatistic(4, "gamesPlayed") - targetStatistics.getStatistic(4, "gamesWon")) + "**;;&aClick to view more!"));
        this.setItem(2, 4, new GUIItem(Material.SNOW_BALL, "&a&lPaintball", 1, ";&r&fGames Played: **" + targetStatistics.getStatistic(3, "gamesPlayed") + "**;&r&fWins: **" + targetStatistics.getStatistic(3, "gamesWon") + "**;&r&fLosses: **" + (targetStatistics.getStatistic(3, "gamesPlayed") - targetStatistics.getStatistic(3, "gamesWon")) + "**;;&aClick to view more!"));
        this.setItem(2, 5, new GUIItem(Material.IRON_SPADE, "&b&lSpleef", 1, ";&r&fGames Played: **" + targetStatistics.getStatistic(100, "gamesPlayed") + "**;&r&fWins: **" + targetStatistics.getStatistic(100, "gamesWon") + "**;&r&fLosses: **" + (targetStatistics.getStatistic(100, "gamesPlayed") - targetStatistics.getStatistic(100, "gamesWon")) + "**;;&aClick to view more!"));
        this.setItem(2, 6, new GUIItem(Material.BAKED_POTATO, "&c&lHotPotato", 1, ";&r&fGames Played: **" + targetStatistics.getStatistic(101, "gamesPlayed") + "**;&r&fWins: **" + targetStatistics.getStatistic(101, "gamesWon") + "**;&r&fLosses: **" + (targetStatistics.getStatistic(101, "gamesPlayed") - targetStatistics.getStatistic(101, "gamesWon")) + "**;;&aClick to view more!"));
        this.setItem(3, 3, new GUIItem(Material.IRON_AXE, "&c&lFFA", 1, ";&r&fGames Played: **" + targetStatistics.getStatistic(102, "gamesPlayed") + "**;&r&fWins: **" + targetStatistics.getStatistic(102, "gamesWon") + "**;&r&fLosses: **" + (targetStatistics.getStatistic(102, "gamesPlayed") - targetStatistics.getStatistic(102, "gamesWon")) + "**;;&aClick to view more!"));
        this.setItem(3, 4, new GUIItem(Material.LEASH, "&c&lTag", 1, ";&r&fGames Played: **" + targetStatistics.getStatistic(103, "gamesPlayed") + "**;&r&fWins: **" + targetStatistics.getStatistic(103, "gamesWon") + "**;&r&fLosses: **" + (targetStatistics.getStatistic(103, "gamesPlayed") - targetStatistics.getStatistic(103, "gamesWon")) + "**;;&aClick to view more!"));
        this.setItem(3, 5, new GUIItem(Material.STAINED_CLAY, "&e&lRun", 1, ";&r&fGames Played: **" + targetStatistics.getStatistic(104, "gamesPlayed") + "**;&r&fWins: **" + targetStatistics.getStatistic(104, "gamesWon") + "**;&r&fLosses: **" + (targetStatistics.getStatistic(104, "gamesPlayed") - targetStatistics.getStatistic(104, "gamesWon")) + "**;;&aClick to view more!", (short)14));

        this.setItem(5, 4, new GUIItem(Material.DIAMOND_BOOTS, "&b&lLobby Parkour", 1, ";&r&fCheckpoints Hit: **" + targetStatistics.getStatistic(0, "pkcheckpoints") + "**;&r&fJumps: **" + targetStatistics.getStatistic(0, "pkjumps") + "**;&r&fDistance Travelled: **" + ((targetStatistics.getStatistic(0, "pkdistance"))/10f) + "**;&r&fTime Spent in Parkour: **" + (new TimeLength(targetStatistics.getStatistic(0, "pktime")/3600000d)) + "**"));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (item.getType() == Material.STAINED_GLASS_PANE) {
            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
            return;
        }
        switch (item.getType()) {
            case BOOK: {
                GeneralStatistics stats = new GeneralStatistics(player, name, this.stats, this.subscription, playerId);
                stats.open(player);
                break;
            }
            case FIREWORK: {
                Achievements stats = new Achievements(player, this.stats, name);
                stats.open(player);
                break;
            }
            case NETHER_STAR: {
                CrystalQuestStatistics stats = new CrystalQuestStatistics(player, name, this.stats, this.subscription, playerId);
                stats.open(player);
                break;
            }
            case IRON_SPADE: {
                SpleefStatistics stats = new SpleefStatistics(player, name, this.stats, this.subscription, playerId);
                stats.open(player);
                break;
            }
            case IRON_AXE: {
                FFAStatistics stats = new FFAStatistics(player, name, this.stats, this.subscription, playerId);
                stats.open(player);
                break;
            }
            case BAKED_POTATO: {
                HotPotatoStatistics stats = new HotPotatoStatistics(player, name, this.stats, this.subscription, playerId);
                stats.open(player);
                break;
            }
            case STAINED_CLAY: {
                RunStatistics stats = new RunStatistics(player, name, this.stats, this.subscription, playerId);
                stats.open(player);
                break;
            }
            case LEASH: {
                TagStatistics stats = new TagStatistics(player, name, this.stats, this.subscription, playerId);
                stats.open(player);
                break;
            }
            case SNOW_BALL: {
                PaintballStatistics stats = new PaintballStatistics(player, name, this.stats, this.subscription, playerId);
                stats.open(player);
                break;
            }
            case IRON_SWORD: {
                DuelsStatistics stats = new DuelsStatistics(player, name, this.stats, this.subscription, playerId);
                stats.open(player);
                break;
            }
            default: {
                player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100, 0);
            }
        }
    }
}
