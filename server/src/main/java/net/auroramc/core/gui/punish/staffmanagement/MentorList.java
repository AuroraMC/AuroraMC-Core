/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.core.gui.punish.staffmanagement;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.player.Mentor;
import net.auroramc.api.punishments.Punishment;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class MentorList extends GUI {

    private AuroraMCServerPlayer player;
    private List<Mentor> mentors;

    public MentorList(AuroraMCServerPlayer player, List<Mentor> mentors) {
        super("&3&lMentors", 2, true);
        this.player = player;
        this.mentors = mentors;

        this.setItem(2, 8, new GUIItem(Material.SKULL_ITEM, "&3&lUnassigned Pending Punishments"));

        int column = 0;
        int row = 0;
        for (Mentor mentor : mentors) {
            this.setItem(row, column, new GUIItem(Material.SKULL_ITEM, String.format("&3&l%s's Mentees", mentor.getName()), 1, String.format("&r&fLook at %s's mentees.", mentor.getName()), (short)3, false, mentor.getName()));
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
            //This is the unassigned punishments, open the menu.
            new BukkitRunnable() {
                @Override
                public void run() {
                    List<Punishment> punishments = AuroraMCAPI.getDbManager().getUnassignedPunishments();
                    PunishmentList gui = new PunishmentList(player, mentors, null, null, punishments);
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            gui.open(player);
                        }
                    }.runTask(ServerAPI.getCore());
                }
            }.runTaskAsynchronously(ServerAPI.getCore());
            return;
        }

        if (item.getType() == Material.SKULL_ITEM) {
            String name = ChatColor.stripColor(item.getItemMeta().getDisplayName()).split("'")[0];
            for (Mentor mentor : mentors) {
                if (mentor.getName().equalsIgnoreCase(name)) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            MenteeList gui = new MenteeList(player, mentors, mentor);
                            new BukkitRunnable(){
                                @Override
                                public void run() {
                                    gui.open(player);
                                }
                            }.runTask(ServerAPI.getCore());
                        }
                    }.runTaskAsynchronously(ServerAPI.getCore());
                    return;
                }
            }
        }
    }
}
