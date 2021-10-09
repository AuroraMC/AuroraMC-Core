/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.gui.punish.staffmanagement;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.Mentee;
import net.auroramc.core.api.players.Mentor;
import net.auroramc.core.api.punishments.Punishment;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class MenteeList extends GUI {

    private AuroraMCPlayer player;
    private List<Mentor> mentors;
    private Mentor clickedMentor;

    public MenteeList(AuroraMCPlayer player, List<Mentor> mentors, Mentor clickedMentor) {
        super(String.format("&3&l%s's Mentees", clickedMentor.getName()), 2, true);

        this.setItem(2, 8, new GUIItem(Material.ARROW, "&3&lView All Mentors"));

        this.player = player;
        this.mentors = mentors;
        this.clickedMentor = clickedMentor;

        int column = 0;
        int row = 0;
        for (Mentee mentee : clickedMentor.getMentees()) {
            List<Punishment> punishments = AuroraMCAPI.getDbManager().getUnprocessedPunishments(mentee.getAmcId());
            this.setItem(row, column, new GUIItem(Material.SKULL_ITEM, String.format("&3&l%s's Pending Punishments", mentee.getName()), 1, String.format("&rLook at %s's pending punishments.", mentee.getName()), (short)3, punishments.size() > 0, mentee.getName()));
            column++;
            if (column == 9) {
                column = 0;
                row++;
            }
        }

    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (row == 2 && column == 8) {
            //This is the go back item.
            new BukkitRunnable(){
                @Override
                public void run() {
                    MentorList gui = new MentorList(player, mentors);
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            gui.open(player);
                            AuroraMCAPI.openGUI(player, gui);
                        }
                    }.runTask(AuroraMCAPI.getCore());
                }
            }.runTaskAsynchronously(AuroraMCAPI.getCore());
            return;
        }

        if (item.getType() == Material.SKULL_ITEM) {
            String name = ChatColor.stripColor(item.getItemMeta().getDisplayName()).split("'")[0];
            for (Mentee mentee : clickedMentor.getMentees()) {
                if (mentee.getName().equalsIgnoreCase(name)) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            List<Punishment> punishments = AuroraMCAPI.getDbManager().getUnprocessedPunishments(mentee.getAmcId());
                            PunishmentList gui = new PunishmentList(player, mentors, clickedMentor, mentee, punishments);
                            new BukkitRunnable(){
                                @Override
                                public void run() {
                                    gui.open(player);
                                    AuroraMCAPI.openGUI(player, gui);
                                }
                            }.runTask(AuroraMCAPI.getCore());
                        }
                    }.runTaskAsynchronously(AuroraMCAPI.getCore());
                    return;
                }
            }
        }
    }
}
