/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.core.gui.cosmetics;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.Cosmetic;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.events.cosmetics.CosmeticDisableEvent;
import net.auroramc.core.api.events.cosmetics.CosmeticEnableEvent;
import net.auroramc.core.api.events.cosmetics.CosmeticSwitchEvent;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.stream.Collectors;

public class CosmeticsListing extends GUI {

    private final AuroraMCServerPlayer player;
    private final List<Cosmetic> cosmetics;
    private int currentPage;
    private final Cosmetic.CosmeticType type;

    public CosmeticsListing(AuroraMCServerPlayer player, Cosmetic.CosmeticType type, ItemStack item) {
        super(String.format("&3&l%s", type.getName()), 5, true);

        this.player = player;
        this.currentPage = 1;
        this.type = type;

        border(String.format("&3&l%s", type.getName()), "");
        this.setItem(0, 4, new GUIItem(item));
        this.setItem(0, 0, new GUIItem(Material.ARROW, "&3&lBack"));

        cosmetics = AuroraMCAPI.getCosmetics().values().stream().filter(cosmetic -> cosmetic.getType() == type).filter(cosmetic -> cosmetic.hasUnlocked(player) || cosmetic.showIfNotUnlocked()).collect(Collectors.toList());
        if (cosmetics.size() > 28) {
            this.setItem(5, 7, new GUIItem(Material.ARROW, "&3&lNext Page"));
        }

        int column = 1;
        int row = 1;
        for (Cosmetic cosmetic : cosmetics) {
            this.setItem(row, column, GUIItem.fromItem(cosmetic.getItem(player)));
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
        if (item.getType() == Material.GRAY_STAINED_GLASS_PANE) {
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 100, 0);
            return;
        }

        if (item.getType() == Material.ARROW) {
            if (row == 0 && column == 0) {
                Cosmetics cosmetics = new Cosmetics(player);
                cosmetics.open(player);
                return;
            }

            if (row == 5) {
                if (column == 1) {
                    currentPage--;
                    this.updateItem(5, 7, new GUIItem(Material.ARROW, "&3&lNext Page"));

                    if (currentPage == 1) {
                        this.updateItem(5, 1, new GUIItem(Material.GRAY_STAINED_GLASS_PANE, String.format("&3&l%s", type.getName()), 1, ""));
                    }
                } else {
                    currentPage++;
                    if (cosmetics.size() < ((currentPage) * 28)) {
                        this.updateItem(5, 7, new GUIItem(Material.GRAY_STAINED_GLASS_PANE, String.format("&3&l%s", type.getName()), 1, ""));
                    }

                    this.updateItem(5, 1, new GUIItem(Material.ARROW, "&3&lPrevious Page"));
                }

                column = 1;
                row = 1;
                for (int i = 0;i < 28;i++) {
                    int pi = (((currentPage - 1) * 28) + i);
                    if (cosmetics.size() <= pi) {
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

                    Cosmetic cosmetic = cosmetics.get(pi);
                    this.updateItem(row, column, GUIItem.fromItem(cosmetic.getItem(player)));
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
            }
        }

        if (row == 0) {
            return;
        }

        //Get clicked cosmetic.
        Cosmetic cosmetic = cosmetics.get(((currentPage - 1) * 28) + ((row - 1) * 7) + (column - 1));
        if (cosmetic.hasUnlocked(player)) {
            if (player.getActiveCosmetics().get(type) != null) {
                if (player.getActiveCosmetics().get(type).equals(cosmetic)) {
                    if (!AuroraMCAPI.isCosmeticsEnabled()) {
                        player.sendMessage(TextFormatter.pluginMessage("Cosmetics", "Cosmetics are currently disabled!"));
                        return;
                    }
                    //disable
                    CosmeticDisableEvent cosmeticDisableEvent = new CosmeticDisableEvent(player, cosmetic);
                    Bukkit.getPluginManager().callEvent(cosmeticDisableEvent);
                    if (cosmeticDisableEvent.isCancelled()) {
                        player.sendMessage(TextFormatter.pluginMessage("Cosmetics", "You currently cannot disable that gadget!"));
                        return;
                    }
                    player.getActiveCosmetics().remove(type);
                    cosmetic.onUnequip(player);
                    player.sendMessage(TextFormatter.pluginMessage("Cosmetics", String.format("You have unequipped **%s**.", TextFormatter.convert(cosmetic.getName()))));
                    if (!AuroraMCAPI.isTestServer()) {
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                AuroraMCAPI.getDbManager().unequipCosmetic(player.getUniqueId(), cosmetic);
                            }
                        }.runTaskAsynchronously(ServerAPI.getCore());
                    }
                } else {
                    //enable and disable old one.
                    if (!AuroraMCAPI.isCosmeticsEnabled()) {
                        player.sendMessage(TextFormatter.pluginMessage("Cosmetics", "Cosmetics are currently disabled!"));
                        return;
                    }
                    CosmeticSwitchEvent cosmeticSwitchEvent = new CosmeticSwitchEvent(player, cosmetic);
                    Bukkit.getPluginManager().callEvent(cosmeticSwitchEvent);
                    if (cosmeticSwitchEvent.isCancelled()) {
                        player.sendMessage(TextFormatter.pluginMessage("Cosmetics", "You currently cannot enable that gadget!"));
                        return;
                    }
                    player.sendMessage(TextFormatter.pluginMessage("Cosmetics", String.format("You have unequipped **%s**.", TextFormatter.convert(player.getActiveCosmetics().get(type).getName()))));
                    Cosmetic prevCosmetic = player.getActiveCosmetics().get(type);
                    player.getActiveCosmetics().put(type, cosmetic);
                    prevCosmetic.onUnequip(player);
                    player.sendMessage(TextFormatter.pluginMessage("Cosmetics", String.format("You have equipped **%s**.", TextFormatter.convert(cosmetic.getName()))));
                    cosmetic.onEquip(player);
                    if (!AuroraMCAPI.isTestServer()) {
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                AuroraMCAPI.getDbManager().unequipCosmetic(player.getUniqueId(), prevCosmetic);
                                AuroraMCAPI.getDbManager().equipCosmetic(player.getUniqueId(), cosmetic);
                            }
                        }.runTaskAsynchronously(ServerAPI.getCore());
                    }
                }
            } else {
                //enable
                if (!AuroraMCAPI.isCosmeticsEnabled()) {
                    player.sendMessage(TextFormatter.pluginMessage("Cosmetics", "Cosmetics are currently disabled!"));
                    return;
                }
                CosmeticEnableEvent cosmeticEnableEvent = new CosmeticEnableEvent(player, cosmetic);
                Bukkit.getPluginManager().callEvent(cosmeticEnableEvent);
                if (cosmeticEnableEvent.isCancelled()) {
                    player.sendMessage(TextFormatter.pluginMessage("Cosmetics", "You currently cannot enable that gadget!"));
                    return;
                }
                for (Cosmetic.CosmeticType type : cosmetic.getType().getConflicts()) {
                    if (player.getActiveCosmetics().containsKey(type)) {
                        Cosmetic cos = player.getActiveCosmetics().get(type);
                        player.getActiveCosmetics().remove(type);
                        cos.onUnequip(player);
                        player.sendMessage(TextFormatter.pluginMessage("Cosmetics", String.format("You have unequipped **%s**.", TextFormatter.convert(cos.getName()))));
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                AuroraMCAPI.getDbManager().unequipCosmetic(player.getUniqueId(), cos);
                            }
                        }.runTaskAsynchronously(ServerAPI.getCore());
                    }
                }
                player.getActiveCosmetics().put(type, cosmetic);
                cosmetic.onEquip(player);
                player.sendMessage(TextFormatter.pluginMessage("Cosmetics", String.format("You have equipped **%s**.", TextFormatter.convert(cosmetic.getName()))));
                if (!AuroraMCAPI.isTestServer()) {
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            AuroraMCAPI.getDbManager().equipCosmetic(player.getUniqueId(), cosmetic);
                        }
                    }.runTaskAsynchronously(ServerAPI.getCore());
                }
            }
            player.closeInventory();
        } else {
            //Check if they can unlock using currency.
        }

    }
}
