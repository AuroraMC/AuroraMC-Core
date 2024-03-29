/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.gui.stats.stats;

import net.auroramc.api.permissions.PlusSubscription;
import net.auroramc.api.stats.PlayerStatistics;
import net.auroramc.api.utils.LevelUtils;
import net.auroramc.api.utils.TimeLength;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import net.auroramc.smp.api.utils.gui.GUI;
import net.auroramc.smp.api.utils.gui.GUIItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;

public class GeneralStatistics extends GUI {

    private final AuroraMCServerPlayer player;
    private final String name;
    private final PlayerStatistics stats;
    private final PlusSubscription subscription;
    private final int playerId;

    public GeneralStatistics(AuroraMCServerPlayer player, String targetName, PlayerStatistics targetStatistics, PlusSubscription subscription, int playerId) {
        super(String.format("&3&l%s's General Statistics", targetName), 5, true);

        this.player = player;
        this.name = targetName;
        this.stats = targetStatistics;
        this.subscription = subscription;
        this.playerId = playerId;

        border(String.format("&3&l%s's Statistics", name), "");
        this.setItem(0, 4, new GUIItem(Material.PLAYER_HEAD, String.format("&3&l%s's Statistics", name), 1, "", (short) 0, false, name));
        this.setItem(0, 0, new GUIItem(Material.ARROW, "&3&lBACK", 1, "&r&fReturn to the statistics menu"));

        TimeLength ingame = new TimeLength(stats.getGameTimeMs()/3600000d, false);
        TimeLength lobby = new TimeLength(stats.getLobbyTimeMs()/3600000d, false);
        TimeLength total = new TimeLength((stats.getGameTimeMs() + stats.getLobbyTimeMs())/3600000d, false);
        this.setItem(2, 2, new GUIItem(Material.CLOCK, "&c&lIn-Game Time", 1, String.format("&r&fTime In-Game: **%s**;&r&fTime In Hub: **%s**;&r&fTotal Time: **%s**", ingame, lobby, total)));

        if (subscription != null) {
            double value = (subscription.getEndTimestamp() - System.currentTimeMillis()) / 3600000d;

            String suffix = "Hours";
            if (value >= 24) {
                suffix = "Days";
                value = value / 24;
            }
            value = (Math.round(value * 10))/10.0;
            double roundedUp = Math.floor(value);
            int totalSub = (int) (subscription.getDaysSubscribed() - roundedUp);
            String expiresIn = value + " " + suffix;
            this.setItem(3, 3, new GUIItem(Material.NETHER_STAR, "&d&lPlus Statistics", 1, ((subscription.getEndTimestamp() != -1)?String.format("&r&fTotal Days Subscribed: **%s**;&r&fCurrent Subscription Streak: **%s**;&r&fExpires in: **%s**", totalSub, subscription.getSubscriptionStreak(), expiresIn):"&r&fNo subscription active.")));
        } else {
            this.setItem(3, 3, new GUIItem(Material.NETHER_STAR, "&d&lPlus Statistics", 1, "&r&fNo subscription active."));
        }

        if (stats.getLevel() == 250) {
            this.setItem(2, 4, new GUIItem(Material.EXPERIENCE_BOTTLE, "&b&lExperience Earned", 1, String.format("&r&fCurrent Level: **Level %s**;&r&fTotal EXP Earned: **%s**;;&r&3&lMAX LEVEL", stats.getLevel(), String.format("%,d", stats.getTotalXpEarned()))));
        } else {
            String progress = "||||||||||||||||||||||||||||||";
            double percentage = (((double) stats.getXpIntoLevel() / LevelUtils.xpForLevel(stats.getLevel() + 1))*100);
            if (stats.getLevel() != 250) {
                int amountToColour = (int) Math.floor(((percentage) / 100)*30);
                progress = ((progress.substring(0, amountToColour) + "&r&f&l" + progress.substring(amountToColour + 1)));
            } else {
                percentage = 100.0;
            }

            this.setItem(2, 4, new GUIItem(Material.EXPERIENCE_BOTTLE, "&b&lExperience Earned", 1, String.format("&r&fCurrent Level: **Level %s**;&r&fTotal EXP Earned: **%s**;;&r&f &3&l«%s» &r&b&l%s&r&f &3&l«%s»;&r&fProgress to Next Level: **%s%%**", stats.getLevel(), String.format("%,d", stats.getTotalXpEarned()), stats.getLevel() - ((stats.getLevel() == 250)?1:0), progress, stats.getLevel() + ((stats.getLevel() != 250)?1:0), new DecimalFormat("##.#").format(percentage))));
        }

        this.setItem(3, 5, new GUIItem(Material.BOOK, "&a&lGames Played", 1, String.format("&r&fTotal Games Played: **%s**;&r&fTotal Games Won: **%s**;&r&fTotal Games Lost: **%s**", stats.getGamesPlayed(), stats.getGamesWon(), stats.getGamesLost())));

        this.setItem(2, 6, new GUIItem(Material.SUNFLOWER, "&e&lCurrency Earned",1, String.format("&r&fTotal Tickets Earned: **%s**;&r&fTotal Crowns Earned: **%s**", stats.getTicketsEarned(), stats.getCrownsEarned())));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (item.getType() == Material.ARROW) {
            Stats stats = new Stats(player, name, this.stats, this.subscription, playerId);
            stats.open(player);
        } else {
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 100, 0);
        }
    }
}
