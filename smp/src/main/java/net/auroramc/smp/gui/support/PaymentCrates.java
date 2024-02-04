/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.gui.support;

import net.auroramc.api.backend.store.Payment;
import net.auroramc.api.cosmetics.Crate;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import net.auroramc.smp.api.utils.gui.GUI;
import net.auroramc.smp.api.utils.gui.GUIItem;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PaymentCrates extends GUI {

    private final AuroraMCServerPlayer player;
    private final UUID uuid;
    private final String name;
    private final List<Payment> payments;

    public PaymentCrates(AuroraMCServerPlayer player, UUID uuid, String name, List<Payment> payments, List<Crate> crates) {
        super(((payments == null)?"&3&lView Your Crates":"&3&lPayment History"), 5, true);
        border(((payments == null)?"&3&lView Your Crates":"&3&lPayment History"), "");

        this.player = player;
        this.uuid = uuid;
        this.payments = payments;
        this.name = name;
        if (payments != null) {
            this.setItem(0, 0, new GUIItem(Material.ARROW, "&a&lGo Back"));
        }
        this.setItem(0, 4, new GUIItem(Material.PLAYER_HEAD, String.format("&3&l%s's Payment History", name), 1, "", (short)0, false, name));

        int column = 1;
        int row = 1;
        for (Crate crate : crates) {
            Material material;
            switch (crate.getType()) {
                case "DIAMOND": {
                    material = Material.DIAMOND;
                    break;
                }
                case "EMERALD": {
                    material = Material.EMERALD;
                    break;
                }
                case "GOLD": {
                    material = Material.GOLD_INGOT;
                    break;
                }
                default: {
                    material = Material.IRON_INGOT;
                    break;
                }
            }
            this.setItem(row, column, new GUIItem(material, "&3&l" + WordUtils.capitalizeFully(crate.getType()) + " Crate", 1, String.format(";&r&fUUID: **%s**;&r&fGenerated: **%s**;&r&fOpened: **%s**;&r&fReward: **%s**;", crate.getUuid().toString(), new Date(crate.getGenerated()).toString(), ((crate.getOpened() != -1)?new Date(crate.getOpened()):"N/A"), ((crate.getLoot() != null)?crate.getLoot().getRewardTitle():"N/A"))));
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
        if (item.getType() == Material.ARROW) {
            PaymentHistory paymentHistory = new PaymentHistory(player, uuid, name, payments);
            paymentHistory.open(player);
        } else {
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 100, 0);
        }
    }
}
