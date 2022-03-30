/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.gui.stats.achievements.game;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.stats.Achievement;
import net.auroramc.core.api.stats.PlayerStatistics;
import net.auroramc.core.api.stats.TieredAcheivement;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import net.auroramc.core.gui.stats.achievements.Achievements;
import net.auroramc.core.gui.stats.achievements.TieredAchievementListing;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GameAchievementListing extends GUI {

    private final List<Achievement> displayOrder;
    private final AuroraMCPlayer player;
    private int currentPage;
    private final PlayerStatistics targetStatistics;
    private final String target;
    private final ItemStack item;
    private final String gameName;

    public GameAchievementListing(AuroraMCPlayer player, PlayerStatistics targetStatistics, String target, ItemStack item, int gameId, String gameName) {
        super("&3&l" + gameName + " Achievements", 5, true);

        this.player = player;
        this.currentPage = 1;
        this.targetStatistics = targetStatistics;
        this.target = target;
        this.item = item;
        this.gameName = gameName;

        long totalAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == gameId).filter((Achievement::isVisible)).count() + targetStatistics.getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == gameId).filter((achievement -> !achievement.isVisible())).count();

        border(String.format("&3&l%s's Achievements", target), "");
        this.setItem(0, 4, new GUIItem(item));
        this.setItem(0, 0, new GUIItem(Material.ARROW, "&3&lBACK"));

        //Get a list of this categories achievements and sort them by whether they have been gained or not.
        Map<Boolean, List<Achievement>> achievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME && achievement.getGameId() == gameId).collect(Collectors.partitioningBy(achievement -> targetStatistics.getAchievementsGained().containsKey(achievement)));

        //Now sort through them and setup the display order.
        displayOrder = new ArrayList<>();
        List<Achievement> achievedNormal = new ArrayList<>();
        List<Achievement> achievedTiered = new ArrayList<>();
        for (Achievement achievement : achievements.get(true)) {
            if (achievement instanceof TieredAcheivement) {
                achievedTiered.add(achievement);
            } else {
                achievedNormal.add(achievement);
            }
        }
        Collections.sort(achievedNormal);
        Collections.sort(achievedTiered);

        List<Achievement> unachievedNormal = new ArrayList<>();
        List<Achievement> unachievedLocked = new ArrayList<>();
        for (Achievement achievement : achievements.get(false)) {
            if (achievement.isVisible()) {
                if (achievement.isLocked()) {
                    unachievedLocked.add(achievement);
                } else {
                    unachievedNormal.add(achievement);
                }
            }
        }
        Collections.sort(unachievedLocked);
        Collections.sort(unachievedNormal);

        displayOrder.addAll(achievedNormal);
        displayOrder.addAll(achievedTiered);
        displayOrder.addAll(unachievedNormal);
        displayOrder.addAll(unachievedLocked);

        if (displayOrder.size() > 28) {
            this.setItem(5, 7, new GUIItem(Material.ARROW, "&3&lNext Page"));
        }

        int column = 1;
        int row = 1;

        for (Achievement achievement : displayOrder) {
            if (achievedNormal.contains(achievement)) {
                this.setItem(row, column, new GUIItem(Material.INK_SACK, "&3&l" + achievement.getName(), 1,  String.format("&rDescription:;&r**%s**;&rRewards: **%s**", WordUtils.wrap(achievement.getDescription(), 40, ";&r&b", false),  achievement.getRewards()), (short)10));
            } else if (achievedTiered.contains(achievement)) {
                this.setItem(row, column, new GUIItem(Material.INK_SACK, "&3&l" + achievement.getName(), 1,  String.format("&rDescription:;&r**%s**;&rRewards: **%s**;;&r&aThis achievement is tiered! Click to view more.", WordUtils.wrap(String.format(achievement.getDescription(), ((TieredAcheivement)achievement).getTiers().get(targetStatistics.getAchievementsGained().get(achievement) - ((targetStatistics.getAchievementsGained().get(achievement) == ((TieredAcheivement)achievement).getTiers().size())?1:0)).getRequirement()), 40, ";&r&b", false),  achievement.getRewards()), ((targetStatistics.getAchievementsGained().get(achievement) == ((TieredAcheivement)achievement).getTiers().size())? (short)10: (short)14)));
            } else if (unachievedNormal.contains(achievement)) {
                this.setItem(row, column, new GUIItem(Material.INK_SACK, "&3&l" + achievement.getName(), 1,  String.format("&rDescription:;&r**%s**;&rRewards: **%s**;;&r&cYou have not achieved this yet." + ((achievement instanceof TieredAcheivement)?";&r&aThis achievement is tiered! Click to view more.":""), WordUtils.wrap(((achievement instanceof TieredAcheivement)?String.format(achievement.getDescription(), (((TieredAcheivement)achievement).getTiers().get(1).getRequirement())):achievement.getDescription()), 40, ";&r&b", false),  achievement.getRewards()), (short)8));
            } else if (unachievedLocked.contains(achievement)) {
                this.setItem(row, column, new GUIItem(Material.INK_SACK, "&c&lAchievement Locked", 1, "&rThis achievement is currently locked.;&rTo unlock it, you must achieve the achievement.", (short)8));
            }

            column++;
            if (column == 8) {
                row++;
                column = 1;
                if (row == 5) {
                    break;
                }
            }
        }
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (item.getType() == Material.ARROW) {
            if (column == 0) {
                GameAchievements achievements = new GameAchievements(player, target, targetStatistics, this.item);
                AuroraMCAPI.closeGUI(player);
                achievements.open(player);
                AuroraMCAPI.openGUI(player, achievements);
                return;
            }
            //Go to next/previous page.
            if (column == 1) {
                //Prev page.
                currentPage--;
                this.updateItem(5, 7, new GUIItem(Material.ARROW, "&3&lNext Page"));

                if (currentPage == 1) {
                    this.updateItem(5, 1, new GUIItem(Material.STAINED_GLASS_PANE, String.format("&3&l%s's Statistics", target), 1, "", (short) 7));
                }
            } else {
                //next page.
                currentPage++;
                if (displayOrder.size() < ((currentPage) * 28)) {
                    this.updateItem(5, 7, new GUIItem(Material.STAINED_GLASS_PANE, String.format("&3&l%s's Statistics", target), 1, "", (short) 7));
                }

                this.updateItem(5, 1, new GUIItem(Material.ARROW, "&3&lPrevious Page"));
            }
            column = 1;
            row = 1;
            for (int i = 0;i < 28;i++) {
                //show the prev 28 items.

                int pi = (((currentPage - 1) * 28) + i);
                if (displayOrder.size() <= pi) {
                    this.updateItem(row, column, null);
                    column++;
                    if (column == 8) {
                        row++;
                        column = 1;
                        if (row == 5) {
                            break;
                        }
                    }
                    continue;
                }

                Achievement achievement = displayOrder.get(pi);
                if (targetStatistics.getAchievementsGained().containsKey(achievement)) {
                    if (!(achievement instanceof TieredAcheivement)) {
                        this.updateItem(row, column, new GUIItem(Material.INK_SACK, "&3&l" + achievement.getName(), 1,  String.format("&rDescription:;&r**%s**;&rRewards: **%s**", WordUtils.wrap(achievement.getDescription(), 40, ";&r&b", false),  achievement.getRewards()), (short)10));
                    } else {
                        this.updateItem(row, column, new GUIItem(Material.INK_SACK, "&3&l" + achievement.getName(), 1,  String.format("&rDescription:;&r**%s**;&rRewards: **%s**;;&r&aThis achievement is tiered! Click to view more.", WordUtils.wrap(String.format(achievement.getDescription(), ((TieredAcheivement)achievement).getTiers().get(targetStatistics.getAchievementsGained().get(achievement) - ((targetStatistics.getAchievementsGained().get(achievement) == ((TieredAcheivement)achievement).getTiers().size())?1:0)).getRequirement()), 40, ";&r&b", false),  achievement.getRewards()), ((targetStatistics.getAchievementsGained().get(achievement) == ((TieredAcheivement)achievement).getTiers().size())?(short)10:(short)14)));
                    }
                } else {
                    if (!achievement.isLocked()) {
                        this.updateItem(row, column, new GUIItem(Material.INK_SACK, "&3&l" + achievement.getName(), 1,  String.format("&rDescription:;&r**%s**;&rRewards: **%s**;;&r&cYou have not achieved this yet.", WordUtils.wrap(((achievement instanceof TieredAcheivement)?String.format(achievement.getDescription(), (((TieredAcheivement)achievement).getTiers().get(1).getRequirement())):achievement.getDescription()), 40, ";&r&b", false),  achievement.getRewards()), (short)8));
                    } else {
                        this.updateItem(row, column, new GUIItem(Material.INK_SACK, "&c&lAchievement Locked", 1, "&rThis achievement is currently locked.;&rTo unlock it, you must achieve the achievement.", (short)8));
                    }
                }
                column++;
                if (column == 8) {
                    row++;
                    column = 1;
                    if (row == 5) {
                        break;
                    }
                }
            }
            return;
        } else if (item.getType() == Material.INK_SACK) {
            if (ChatColor.stripColor(item.getItemMeta().getLore().get(item.getItemMeta().getLore().size() - 1)).equalsIgnoreCase("This achievement is tiered! Click to view more.")) {
                //Display achievement progress
                Achievement achievement = AuroraMCAPI.getAchievement(ChatColor.stripColor(item.getItemMeta().getDisplayName()));
                if (achievement != null) {
                    if (achievement instanceof TieredAcheivement) {
                        TieredAchievementListing listing = new TieredAchievementListing(player, targetStatistics, target, achievement, item, this.item, gameName);
                        AuroraMCAPI.closeGUI(player);
                        listing.open(player);
                        AuroraMCAPI.openGUI(player, listing);
                        return;
                    }
                }
            }
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
        }
    }

}
