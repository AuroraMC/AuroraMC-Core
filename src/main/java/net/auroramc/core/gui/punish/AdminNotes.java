/*
 * Copyright (c) 2021-2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.gui.punish;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.punishments.AdminNote;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;
import java.util.List;

public class AdminNotes extends GUI {

    private final AuroraMCPlayer player;
    private final String name;
    private final int id;
    private final String extraDetails;
    private final List<AdminNote> notes;
    private int page;

    public AdminNotes(AuroraMCPlayer player, String name, int id, String extraDetails) {
        super(String.format("&4&l%s's Admin Notes", name), 5, true);

        this.extraDetails = extraDetails;
        this.name = name;
        this.id = id;
        this.player = player;
        this.notes = AuroraMCAPI.getDbManager().getAdminNotes(id);
        page = 1;

        border(String.format("&3&l%s's Admin Notes", name), "");

        this.setItem(0, 4, new GUIItem(Material.SKULL_ITEM, String.format("&3&l%s's Admin Notes", name), 1, "&rAdmin Notes", (short) 3, false, name));
        if (extraDetails != null) {
            this.setItem(5, 4, new GUIItem(Material.WOOL, "&a&lAdd Note", 1, "&rAdd your current reason as;&ra note!", (short) 5));
        }

        if (notes.size() > 28) {
            this.setItem(5, 7, new GUIItem(Material.ARROW, "&3&lNext Page"));
        }

        int column = 1;
        int row = 1;

        for (AdminNote note : notes) {
            String reason = WordUtils.wrap(note.getNote(), 40, ";&b", false);
            String lore = String.format("&rNote:;" +
                    "**%s**;" +
                    "&rIssued at: **%s**;" +
                    "&rIssued by: **%s**", reason, (new Date(note.getTimestamp())).toString(), note.getAddedBy());
            this.setItem(row, column, new GUIItem(Material.BOOK, "&7&lAdmin Note", 1, lore));
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
        if (item.getType() == Material.WOOL) {
            if (extraDetails != null) {
                player.getPlayer().closeInventory();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        AuroraMCAPI.getDbManager().addNewNote(id, player.getId(), System.currentTimeMillis(), extraDetails);
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Notes", "Admin note added."));
                    }
                }.runTaskAsynchronously(AuroraMCAPI.getCore());
            } else {
                player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
            }
        } else if (item.getType() == Material.ARROW) {
            //Go to next/previous page.
            if (column == 1) {
                //Prev page.
                page--;
                this.updateItem(5, 7, new GUIItem(Material.ARROW, "&3&lNext Page"));

                if (page == 1) {
                    this.updateItem(5, 1, new GUIItem(Material.STAINED_GLASS_PANE, String.format("&3&l%s's Admin Notes", this.name), 1, "", (short) 7));
                }
            } else {
                //next page.
                page++;
                if (notes.size() < ((page) * 28)) {
                    this.updateItem(5, 7, new GUIItem(Material.STAINED_GLASS_PANE, String.format("&3&l%s's Admin Notes", this.name), 1, "", (short) 7));
                }

                this.updateItem(5, 1, new GUIItem(Material.ARROW, "&3&lPrevious Page"));
            }
            column = 1;
            row = 1;
            for (int i = 0; i < 28; i++) {
                //show the prev 28 items.

                int pi = (((page - 1) * 28) + i);
                if (notes.size() <= pi) {
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

                AdminNote note = notes.get(pi);
                String reason = WordUtils.wrap(note.getNote(), 40, ";&b", false);
                String lore = String.format("&rNote:;" +
                        "**%s**;" +
                        "&rIssued at: **%s**;" +
                        "&rIssued by: **%s**", reason, (new Date(note.getTimestamp())).toString(), note.getAddedBy());
                this.updateItem(row, column, new GUIItem(Material.BOOK, "&7&lAdmin Note", 1, lore));
                column++;
                if (column == 8) {
                    row++;
                    column = 1;
                    if (row == 5) {
                        break;
                    }
                }
            }
        } else {
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
        }
    }
}
