/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.core.gui.punish;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
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

    private final AuroraMCServerPlayer player;
    private final String name;
    private final int id;
    private final String extraDetails;

    public Punish(AuroraMCServerPlayer player, String name, int id, String extraDetails) {
        super(String.format("&3&lPunish %s", name), 2, true);

        this.extraDetails = extraDetails;
        this.name = name;
        this.id = id;
        this.player = player;

        this.setItem(0, 4, new GUIItem(Material.PLAYER_HEAD, String.format("&3&lPunish %s", name), 1, "&r&fPlease choose a punishment type.", (short)0, false, name));

        this.setItem(1, 2, new GUIItem(Material.WRITABLE_BOOK, "&3&lChat Rules", 1, String.format("&r&fPunish %s for a chat rule", name)));
        GUIItem guiItem = new GUIItem(Material.IRON_SWORD, "&3&lGame Rules", 1, String.format("&r&fPunish %s for a game rule", name));
        ItemStack itemStack = guiItem.getItemStack();
        ItemMeta meta = itemStack.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(meta);
        this.setItem(1, 4, new GUIItem(itemStack));
        this.setItem(1, 6, new GUIItem(Material.OAK_SIGN, "&3&lMisc Rules", 1, String.format("&r&fPunish %s for a miscellaneous rule", name)));

        if (player.getRank().hasPermission("admin")) {
            this.setItem(2, 3, new GUIItem(Material.REDSTONE_BLOCK, "&4&lGlobal Account Suspension", 1, String.format("&r&fGlobally suspend %s's access to AuroraMC Services", name)));
            this.setItem(2, 5, new GUIItem(Material.PAPER, "&7&lAdmin Notes", 1, String.format("&r&fView %s's notes", name)));
        }
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        int type;
        switch (item.getType()) {
            case WRITABLE_BOOK:
                type = 1;
                break;
            case IRON_SWORD:
                type = 2;
                break;
            case OAK_SIGN:
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
                                adminNotes.open(player);
                            }
                        }.runTask(ServerAPI.getCore());
                    }
                }.runTaskAsynchronously(ServerAPI.getCore());
                return;
            case REDSTONE_BLOCK:
                GlobalAccountSuspension suspension = new GlobalAccountSuspension(player, name, id, extraDetails);
                suspension.open(player);
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
                            ruleListing.open(player);
                        }
                    }.runTask(ServerAPI.getCore());
                }
            }.runTaskAsynchronously(ServerAPI.getCore());
        } else {
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 100, 0);
        }
    }
}
