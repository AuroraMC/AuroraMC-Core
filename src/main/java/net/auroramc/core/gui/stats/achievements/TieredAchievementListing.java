package net.auroramc.core.gui.stats.achievements;

import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.stats.Achievement;
import net.auroramc.core.api.stats.AchievementTier;
import net.auroramc.core.api.stats.TieredAcheivement;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class TieredAchievementListing extends GUI {

    private final AuroraMCPlayer player;

    public TieredAchievementListing(AuroraMCPlayer player, Achievement achievement, ItemStack item) {
        super("&3&l" + achievement.getName(), 2, true);
        for (int i = 0; i <= 8; i++) {
            if (i < 2) {
                this.setItem(i, 0, new GUIItem(Material.STAINED_GLASS_PANE, String.format("&3&l%s's Achievements", player.getPlayer().getName()), 1, "", (short) 7));
                this.setItem(i, 8, new GUIItem(Material.STAINED_GLASS_PANE, String.format("&3&l%s's Achievements", player.getPlayer().getName()), 1, "", (short) 7));
            }
            this.setItem(0, i, new GUIItem(Material.STAINED_GLASS_PANE, String.format("&3&l%s's Achievements", player.getPlayer().getName()), 1, "", (short) 7));
            this.setItem(2, i, new GUIItem(Material.STAINED_GLASS_PANE, String.format("&3&l%s's Achievements", player.getPlayer().getName()), 1, "", (short) 7));
        }
        item.setType(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        lore.remove(lore.size() - 1);
        meta.setLore(lore);
        item.setItemMeta(meta);
        item.setDurability((short)0);
        this.setItem(0, 4, new GUIItem(item));
        this.player = player;

        int column = 1;
        for (AchievementTier tier : ((TieredAcheivement)achievement).getTiers()) {
            if (player.getStats().getAchievementsGained().containsKey(achievement)) {
                int achievedTier = player.getStats().getAchievementsGained().get(achievement);
                if (tier.getTier() <= achievedTier) {
                    this.setItem(1, column, new GUIItem(Material.INK_SACK, "&3&lTier " + tier.getTier(), 1, "&r" + WordUtils.wrap(String.format(achievement.getDescription(), tier.getRequirement()) + ";;&r&aAchieved!", 40, ";&r", false), (short)10));
                } else {
                    this.setItem(1, column, new GUIItem(Material.INK_SACK, "&3&lTier " + tier.getTier(), 1, "&r" + WordUtils.wrap(String.format(achievement.getDescription(), tier.getRequirement()), 40, ";&r", false) + String.format(";;&r&rProgress: **%s/%s**", ((player.getStats().getAchievementProgress().containsKey(achievement))?player.getStats().getAchievementProgress().get(achievement):0), tier.getRequirement()), (short)8));
                }
            } else {
                this.setItem(1, column, new GUIItem(Material.INK_SACK, "&3&lTier " + tier.getTier(), 1, "&r" + WordUtils.wrap(String.format(achievement.getDescription(), tier.getRequirement()), 40, ";&r", false) + String.format(";;&r&rProgress: **%s/%s**", ((player.getStats().getAchievementProgress().containsKey(achievement))?player.getStats().getAchievementProgress().get(achievement):0), tier.getRequirement()), (short)8));
            }
            column++;
        }
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
    }
}
