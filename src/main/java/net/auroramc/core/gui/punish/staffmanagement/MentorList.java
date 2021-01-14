package net.auroramc.core.gui.punish.staffmanagement;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
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

public class MentorList extends GUI {

    private AuroraMCPlayer player;
    private List<Mentor> mentors;

    public MentorList(AuroraMCPlayer player, List<Mentor> mentors) {
        super("&3&lMentors", 2, true);
        this.player = player;
        this.mentors = mentors;

        this.setItem(2, 8, new GUIItem(Material.SKULL_ITEM, "&3&lUnassigned Pending Punishments"));

        int column = 0;
        int row = 0;
        for (Mentor mentor : mentors) {
            this.setItem(row, column, new GUIItem(Material.SKULL_ITEM, String.format("&3&l%s's Mentees", mentor.getName()), 1, String.format("&rLook at %s's mentees.", mentor.getName()), (short)3, false, mentor.getName()));
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
                            AuroraMCAPI.openGUI(player, gui);
                        }
                    }.runTask(AuroraMCAPI.getCore());
                }
            }.runTaskAsynchronously(AuroraMCAPI.getCore());
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
