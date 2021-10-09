/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.gui.punish;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class Punish extends GUI {

    private final AuroraMCPlayer player;
    private final String name;
    private final int id;
    private final String extraDetails;



    public Punish(AuroraMCPlayer player, String name, int id, String extraDetails) {
        super(String.format("&3&lPunish %s", name), 2, true);

        this.extraDetails = extraDetails;
        this.name = name;
        this.id = id;
        this.player = player;

        this.setItem(0, 4, new GUIItem(Material.SKULL_ITEM, String.format("&3&lPunish %s", name), 1, "&rPlease choose a punishment type.", (short)3, false, name));

        this.setItem(1, 2, new GUIItem(Material.BOOK_AND_QUILL, "&3&lChat Rules", 1, String.format("&rPunish %s for a chat rule", name)));
        GUIItem guiItem = new GUIItem(Material.IRON_SWORD, "&3&lGame Rules", 1, String.format("&rPunish %s for a game rule", name));
        ItemStack itemStack = guiItem.getItem();
        ItemMeta meta = itemStack.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(meta);
        this.setItem(1, 4, new GUIItem(itemStack));
        this.setItem(1, 6, new GUIItem(Material.SIGN, "&3&lMisc Rules", 1, String.format("&rPunish %s for a miscellaneous rule", name)));

        if (player.getRank().hasPermission("admin")) {
            this.setItem(2, 3, new GUIItem(Material.REDSTONE_BLOCK, "&4&lGlobal Account Suspension", 1, String.format("&rGlobally suspend %s's access to AuroraMC Services", name)));
            this.setItem(2, 5, new GUIItem(Material.PAPER, "&7&lAdmin Notes", 1, String.format("&rView %s's notes", name)));
        }
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        int type;
        switch (item.getType()) {
            case BOOK_AND_QUILL:
                type = 1;
                break;
            case IRON_SWORD:
                type = 2;
                break;
            case SIGN:
                type = 3;
                break;
            case PAPER:
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        AdminNotes adminNotes = new AdminNotes(player, name, id, extraDetails);
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                AuroraMCAPI.closeGUI(player);
                                adminNotes.open(player);
                                AuroraMCAPI.openGUI(player, adminNotes);
                            }
                        }.runTask(AuroraMCAPI.getCore());
                    }
                }.runTaskAsynchronously(AuroraMCAPI.getCore());
                return;
            case REDSTONE_BLOCK:
                GlobalAccountSuspension suspension = new GlobalAccountSuspension(player, name, id, extraDetails);
                AuroraMCAPI.closeGUI(player);
                suspension.open(player);
                AuroraMCAPI.openGUI(player, suspension);
                return;
            default:
                type = -1;
        }

        if (type != -1) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    RuleListing ruleListing = new RuleListing(player, name, id, type, extraDetails, AuroraMCAPI.getDbManager().getPunishmentHistory(id));
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            AuroraMCAPI.closeGUI(player);
                            ruleListing.open(player);
                            AuroraMCAPI.openGUI(player, ruleListing);
                        }
                    }.runTask(AuroraMCAPI.getCore());
                }
            }.runTaskAsynchronously(AuroraMCAPI.getCore());
        } else {
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
        }
    }
}
