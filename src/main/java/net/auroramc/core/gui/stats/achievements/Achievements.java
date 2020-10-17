package net.auroramc.core.gui.stats.achievements;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.stats.Achievement;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class Achievements extends GUI {

    private final AuroraMCPlayer player;

    public Achievements(AuroraMCPlayer player) {
        super("&3&lAchievements", 5, true);
        int column = 1;
        int row = 1;

        this.player = player;

        long totalAchievements = AuroraMCAPI.getAchievements().values().stream().filter((Achievement::isVisible)).count() + player.getStats().getAchievementsGained().keySet().stream().filter((achievement -> !achievement.isVisible())).count();

        for (int i = 0; i <= 8; i++) {
            if (i < 6) {
                this.setItem(i, 0, new GUIItem(Material.STAINED_GLASS_PANE, String.format("&3&l%s's Achievements", player.getPlayer().getName()), 1, "", (short) 7));
                this.setItem(i, 8, new GUIItem(Material.STAINED_GLASS_PANE, String.format("&3&l%s's Achievements", player.getPlayer().getName()), 1, "", (short) 7));
            }
            this.setItem(0, i, new GUIItem(Material.STAINED_GLASS_PANE, String.format("&3&l%s's Achievements", player.getPlayer().getName()), 1, "", (short) 7));
            this.setItem(5, i, new GUIItem(Material.STAINED_GLASS_PANE, String.format("&3&l%s's Achievements", player.getPlayer().getName()), 1, "", (short) 7));
        }
        this.setItem(0, 4, new GUIItem(Material.SKULL_ITEM, String.format("&3&l%s's Achievements", player.getPlayer().getName()), 1, String.format(";&rAchieved: **%s**;&rTotal Achievements: **%s**", player.getStats().getAchievementsGained().size(), totalAchievements), (short) 3, false, player.getPlayer().getName()));

        long totalGeneralAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GENERAL).filter((Achievement::isVisible)).count() + player.getStats().getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GENERAL).filter((achievement -> !achievement.isVisible())).count();
        long totalFriendsAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.FRIENDS).filter((Achievement::isVisible)).count() + player.getStats().getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.FRIENDS).filter((achievement -> !achievement.isVisible())).count();
        long totalPartyAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.PARTY).filter((Achievement::isVisible)).count() + player.getStats().getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.PARTY).filter((achievement -> !achievement.isVisible())).count();
        long totalTimeAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.TIME).filter((Achievement::isVisible)).count() + player.getStats().getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.TIME).filter((achievement -> !achievement.isVisible())).count();
        long totalLoyaltyAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.LOYALTY).filter((Achievement::isVisible)).count() + player.getStats().getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.LOYALTY).filter((achievement -> !achievement.isVisible())).count();
        long totalExperienceAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.EXPERIENCE).filter((Achievement::isVisible)).count() + player.getStats().getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.EXPERIENCE).filter((achievement -> !achievement.isVisible())).count();
        long totalGameAchievements = AuroraMCAPI.getAchievements().values().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME).filter((Achievement::isVisible)).count() + player.getStats().getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME).filter((achievement -> !achievement.isVisible())).count();
        this.setItem(2, 2, new GUIItem(Material.SIGN, "&3&lGeneral Achievements", 1, String.format("&rAchieved: **%s**;&rTotal Achievements: **%s**", player.getStats().getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GENERAL).count(), totalGeneralAchievements)));
        this.setItem(2, 3, new GUIItem(Material.SKULL_ITEM, "&d&lFriends Achievements", 1, String.format("&rAchieved: **%s**;&rTotal Achievements: **%s**", player.getStats().getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.FRIENDS).count(), totalFriendsAchievements)));
        this.setItem(2, 4, new GUIItem(Material.FIREWORK, "&6&lParty Achievements", 1, String.format("&rAchieved: **%s**;&rTotal Achievements: **%s**", player.getStats().getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.PARTY).count(), totalPartyAchievements)));
        this.setItem(2, 5, new GUIItem(Material.WATCH, "&c&lTime Achievements", 1, String.format("&rAchieved: **%s**;&rTotal Achievements: **%s**", player.getStats().getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.TIME).count(), totalTimeAchievements)));
        this.setItem(2, 6, new GUIItem(Material.RED_ROSE, "&a&lLoyalty Achievements", 1, String.format("&rAchieved: **%s**;&rTotal Achievements: **%s**", player.getStats().getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.LOYALTY).count(), totalLoyaltyAchievements)));
        this.setItem(3, 3, new GUIItem(Material.EXP_BOTTLE, "&b&lExperience Achievements", 1, String.format("&rAchieved: **%s**;&rTotal Achievements: **%s**", player.getStats().getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.EXPERIENCE).count(), totalExperienceAchievements)));
        this.setItem(3, 4, new GUIItem(Material.MINECART, "&e&lGame Achievements", 1, String.format("&rAchieved: **%s**;&rTotal Achievements: **%s**", player.getStats().getAchievementsGained().keySet().stream().filter(achievement -> achievement.getCategory() == Achievement.AchievementCategory.GAME).count(), totalGameAchievements)));
        this.setItem(3, 5, new GUIItem(Material.BEACON, "&4&lComing Soon...", 1, "&rThis category of achievements is coming soon..."));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        Achievement.AchievementCategory category;
        switch (item.getType()) {
            case SIGN:
                category = Achievement.AchievementCategory.GENERAL;
                break;
            case SKULL_ITEM:
                if (row == 0 && column == 4) {
                    player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                    return;
                }
                category = Achievement.AchievementCategory.FRIENDS;
                break;
            case FIREWORK:
                category = Achievement.AchievementCategory.PARTY;
                break;
            case WATCH:
                category = Achievement.AchievementCategory.TIME;
                break;
            case RED_ROSE:
                category = Achievement.AchievementCategory.LOYALTY;
                break;
            case EXP_BOTTLE:
                category = Achievement.AchievementCategory.EXPERIENCE;
                break;
            case MINECART:
                category = Achievement.AchievementCategory.GAME;
                break;
            default:
                player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
                return;
        }

        AchievementListing listing = new AchievementListing(player, category, item);
        AuroraMCAPI.closeGUI(player);
        listing.open(player);
        AuroraMCAPI.openGUI(player, listing);
    }
}
