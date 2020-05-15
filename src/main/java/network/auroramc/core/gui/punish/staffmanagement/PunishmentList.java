package network.auroramc.core.gui.punish.staffmanagement;

import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.players.AuroraMCPlayer;
import network.auroramc.core.api.players.Mentee;
import network.auroramc.core.api.players.Mentor;
import network.auroramc.core.api.punishments.Punishment;
import network.auroramc.core.api.punishments.PunishmentLength;
import network.auroramc.core.api.punishments.Rule;
import network.auroramc.core.api.utils.gui.GUI;
import network.auroramc.core.api.utils.gui.GUIItem;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PunishmentList extends GUI {

    private final List<Punishment> punishments;
    private final AuroraMCPlayer player;
    private int currentPage;
    private final List<Mentor> mentors;
    private final Mentor clickedMentor;
    private final Mentee clickedMentee;

    public PunishmentList(AuroraMCPlayer player, List<Mentor> mentors, Mentor clickedMentor, Mentee clickedMentee, List<Punishment> punishments) {
        super(((clickedMentee != null)?String.format("&3&l%s's Pending Punishments", clickedMentee.getName()):"&3&lUnassigned Pending Punishments"), 5, true);

        this.currentPage = 1;
        this.player = player;
        this.mentors = mentors;
        this.clickedMentee = clickedMentee;
        this.clickedMentor = clickedMentor;


        int column = 1;
        int row = 1;
        this.punishments = punishments;

        for (int i = 0; i <= 8; i++) {
            if (i < 6) {
                this.setItem(i, 0, new GUIItem(Material.STAINED_GLASS_PANE, ((clickedMentee != null)?String.format("&3&l%s's Pending Punishments", clickedMentee.getName()):"&3&lUnassigned Pending Punishments"), 1, "", (short) 7));
                this.setItem(i, 8, new GUIItem(Material.STAINED_GLASS_PANE, ((clickedMentee != null)?String.format("&3&l%s's Pending Punishments", clickedMentee.getName()):"&3&lUnassigned Pending Punishments"), 1, "", (short) 7));
            }
            this.setItem(0, i, new GUIItem(Material.STAINED_GLASS_PANE, ((clickedMentee != null)?String.format("&3&l%s's Pending Punishments", clickedMentee.getName()):"&3&lUnassigned Pending Punishments"), 1, "", (short) 7));
            this.setItem(5, i, new GUIItem(Material.STAINED_GLASS_PANE, ((clickedMentee != null)?String.format("&3&l%s's Pending Punishments", clickedMentee.getName()):"&3&lUnassigned Pending Punishments"), 1, "", (short) 7));
        }
        this.setItem(0, 4, new GUIItem(Material.SKULL_ITEM, ((clickedMentee != null)?String.format("&3&l%s's Pending Punishments", clickedMentee.getName()):"&3&lUnassigned Pending Punishments"), 1, "&rPending Punishments", (short) 3, false, ((clickedMentee==null)?null:clickedMentee.getName())));
        this.setItem(0, 0, new GUIItem(Material.ARROW, "&3&lView Mentee List"));
        if (punishments.size() > 28) {
            this.setItem(5, 7, new GUIItem(Material.ARROW, "&3&lNext Page"));
        }
        for (Punishment punishment : punishments) {
            Rule rule = AuroraMCAPI.getRules().getRule(punishment.getRuleID());
            String reason;
            switch (punishment.getStatus()) {
                case 2:
                    reason = WordUtils.wrap(String.format("%s - %s [Awaiting Approval]", rule.getRuleName(), punishment.getExtraNotes()), 43, ";&b", false);
                    break;
                case 6:
                case 3:
                    reason = WordUtils.wrap(String.format("%s - %s [SM Approved]&r", rule.getRuleName(), punishment.getExtraNotes()), 43, ";&b", false);
                    break;
                default:
                    reason = WordUtils.wrap(String.format("&b%s - %s", rule.getRuleName(), punishment.getExtraNotes()), 43, ";&b", false);
            }

            PunishmentLength length;
            if (punishment.getExpire() == -1) {
                length = new PunishmentLength(-1);
            } else {
                length = new PunishmentLength((punishment.getExpire() - punishment.getIssued()) / 3600000d);
            }

            String[] weights = new String[]{"Light", "Medium", "Heavy", "Severe", "Extreme"};
            String lore = String.format("&rPunished: **%s**;&rPunishment Code: **%s**;&rReason:;**%s**;&rWeight: **%s**;&rIssued at: **%s**;&rLength: **%s**" + ((clickedMentee == null) ? ";&rIssued by: **%s**" : ""), punishment.getPunishedName(), punishment.getPunishmentCode(), reason, weights[rule.getWeight() - 1], (new Date(punishment.getIssued())).toString(), length.getFormatted(), punishment.getPunisherName());

            switch (rule.getType()) {
                case 1:
                    this.setItem(row, column, new GUIItem(Material.BOOK_AND_QUILL, "&3&lChat Offence", 1, lore));
                    break;
                case 2:
                    this.setItem(row, column, new GUIItem(Material.IRON_SWORD, "&3&lGame Offence", 1, lore));
                    break;
                case 3:
                    this.setItem(row, column, new GUIItem(Material.SIGN, "&3&lMisc Offence", 1, lore));
                    break;
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
        if (row == 0 && column == 0) {
            //Return to wherever they came from
            if (clickedMentee == null) {
                //They came from the main menu.
                MentorList gui = new MentorList(player, mentors);
                gui.open(player);
                AuroraMCAPI.openGUI(player, gui);
            } else {
                //They came from the mentee selection menu.
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        MenteeList gui = new MenteeList(player, mentors, clickedMentor);
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                gui.open(player);
                                AuroraMCAPI.openGUI(player, gui);
                            }
                        }.runTask(AuroraMCAPI.getCore());
                    }
                }.runTaskAsynchronously(AuroraMCAPI.getCore());
            }
        }

        if (item.getType() == Material.ARROW) {
            //Go to next/previous page.
            if (column == 1) {
                //Prev page.
                currentPage--;
                this.updateItem(5, 7, new GUIItem(Material.ARROW, "&3&lNext Page"));

                if (currentPage == 1) {
                    this.updateItem(5, 1, new GUIItem(Material.STAINED_GLASS_PANE, ((clickedMentee != null)?String.format("&3&l%s's Pending Punishments", clickedMentee.getName()):"&3&lUnassigned Pending Punishments"), 1, "", (short) 7));
                }
            } else {
                //next page.
                currentPage++;
                if (punishments.size() < ((currentPage) * 28)) {
                    this.updateItem(5, 7, new GUIItem(Material.STAINED_GLASS_PANE, ((clickedMentee != null)?String.format("&3&l%s's Pending Punishments", clickedMentee.getName()):"&3&lUnassigned Pending Punishments"), 1, "", (short) 7));
                }

                this.updateItem(5, 1, new GUIItem(Material.ARROW, "&3&lPrevious Page"));
            }
            column = 1;
            row = 1;
            for (int i = 0;i < 28;i++) {
                //show the prev 28 items.

                int pi = (((currentPage - 1) * 28) + i);
                if (punishments.size() <= pi) {
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

                Punishment punishment = punishments.get(pi);

                Rule rule = AuroraMCAPI.getRules().getRule(punishment.getRuleID());
                String reason;
                switch (punishment.getStatus()) {
                    case 2:
                        reason = WordUtils.wrap(String.format("%s - %s [Awaiting Approval]", rule.getRuleName(), punishment.getExtraNotes()), 43, ";&b", false);
                        break;
                    case 6:
                    case 3:
                        reason = WordUtils.wrap(String.format("%s - %s [SM Approved]&r", rule.getRuleName(), punishment.getExtraNotes()), 43, ";&b", false);
                        break;
                    default:
                        reason = WordUtils.wrap(String.format("&b%s - %s", rule.getRuleName(), punishment.getExtraNotes()), 43, ";&b", false);
                }

                PunishmentLength length;
                if (punishment.getExpire() == -1) {
                    length = new PunishmentLength(-1);
                } else {
                    length = new PunishmentLength((punishment.getExpire() - punishment.getIssued()) / 3600000d);
                }

                String[] weights = new String[]{"Light", "Medium", "Heavy", "Severe", "Extreme"};
                String lore = String.format("&rPunishment Code: **%s**;&rReason:;**%s**;&rWeight: **%s**;&rIssued at: **%s**;&rLength: **%s**" + ((clickedMentee == null) ? ";&rIssued by: **%s**" : ""), punishment.getPunishmentCode(), reason, weights[rule.getWeight() - 1], (new Date(punishment.getIssued())).toString(), length.getFormatted(), punishment.getPunisherName());

                switch (rule.getType()) {
                    case 1:
                        this.setItem(row, column, new GUIItem(Material.BOOK_AND_QUILL, "&3&lChat Offence", 1, lore));
                        break;
                    case 2:
                        this.setItem(row, column, new GUIItem(Material.IRON_SWORD, "&3&lGame Offence", 1, lore));
                        break;
                    case 3:
                        this.setItem(row, column, new GUIItem(Material.SIGN, "&3&lMisc Offence", 1, lore));
                        break;
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
        } else if (item.getType() == Material.STAINED_GLASS_PANE || item.getType() == Material.SKULL_ITEM) {
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
            return;
        } else {
            //Get the clicked punishment.
            Punishment punishment = punishments.get(((currentPage - 1) * 28) + ((row - 1) * 7) + (column - 1));

            ApprovalGUI gui = new ApprovalGUI(player, mentors, clickedMentor, clickedMentee, punishments, punishment);
            gui.open(player);
            AuroraMCAPI.openGUI(player, gui);
        }
    }
}
