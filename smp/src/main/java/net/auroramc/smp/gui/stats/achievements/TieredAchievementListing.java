/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.gui.stats.achievements;

import net.auroramc.api.stats.Achievement;
import net.auroramc.api.stats.AchievementTier;
import net.auroramc.api.stats.PlayerStatistics;
import net.auroramc.api.stats.TieredAcheivement;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import net.auroramc.smp.api.utils.gui.GUI;
import net.auroramc.smp.api.utils.gui.GUIItem;
import net.auroramc.smp.gui.stats.achievements.game.GameAchievementListing;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class TieredAchievementListing extends GUI {

    private final AuroraMCServerPlayer player;
    private final PlayerStatistics targetStatistics;
    private final String target;
    private final ItemStack item;
    private final Achievement achievement;
    private final String gameName;

    public TieredAchievementListing(AuroraMCServerPlayer player, PlayerStatistics targetStatistics, String target, Achievement achievement, ItemStack item, ItemStack prevItem, String gameName) {
        super("&3&l" + achievement.getName(), 2, true);
        border(String.format("&3&l%s's Achievements", target), "");
        item.setType(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        lore.remove(lore.size() - 1);
        meta.setLore(lore);
        item.setItemMeta(meta);
        item.setDurability((short)0);
        this.setItem(0, 4, new GUIItem(item));
        this.setItem(0, 0, new GUIItem(Material.ARROW, "&3&lBACK"));
        this.player = player;
        this.target = target;
        this.targetStatistics = targetStatistics;
        this.item = prevItem;
        this.achievement = achievement;
        this.gameName = gameName;

        int column = 1;
        for (AchievementTier tier : ((TieredAcheivement)achievement).getTiers()) {
            if (targetStatistics.getAchievementsGained().containsKey(achievement)) {
                int achievedTier = targetStatistics.getAchievementsGained().get(achievement);
                if (tier.getTier() <= achievedTier) {
                    this.setItem(1, column, new GUIItem(Material.LIME_DYE, "&3&lTier " + tier.getTier(), 1, "&r&f" + WordUtils.wrap(String.format(achievement.getDescription(), tier.getRequirement()) + ";;&r&aAchieved!", 40, ";&r&f", false)));
                } else {
                    this.setItem(1, column, new GUIItem(Material.GRAY_DYE, "&3&lTier " + tier.getTier(), 1, "&r&f" + WordUtils.wrap(String.format(achievement.getDescription(), tier.getRequirement()), 40, ";&r&f", false) + String.format(";;&r&f&r&fProgress: **%s/%s**", ((targetStatistics.getAchievementProgress().containsKey(achievement))?targetStatistics.getAchievementProgress().get(achievement):0), tier.getRequirement())));
                }
            } else {
                this.setItem(1, column, new GUIItem(Material.GRAY_DYE, "&3&lTier " + tier.getTier(), 1, "&r&f" + WordUtils.wrap(String.format(achievement.getDescription(), tier.getRequirement()), 40, ";&r&f", false) + String.format(";;&r&f&r&fProgress: **%s/%s**", ((targetStatistics.getAchievementProgress().containsKey(achievement))?targetStatistics.getAchievementProgress().get(achievement):0), tier.getRequirement()), (short)8));
            }
            column++;
        }
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (item.getType() == Material.ARROW) {
            if (achievement.getCategory() == Achievement.AchievementCategory.GAME) {
                GameAchievementListing achievements = new GameAchievementListing(player, targetStatistics, target, this.item, achievement.getGameId(), gameName);
                achievements.open(player);
            } else {
                AchievementListing achievements = new AchievementListing(player, targetStatistics, target, achievement.getCategory(), this.item);
                achievements.open(player);
            }
            return;
        }
        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 100, 0);
    }
}
