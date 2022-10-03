/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.gui.support;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.backend.store.Payment;
import net.auroramc.core.api.cosmetics.Crate;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PaymentHistory extends GUI {

    private final AuroraMCPlayer player;
    private final List<Payment> payments;
    private final UUID uuid;
    private final String name;

    public PaymentHistory(AuroraMCPlayer player, UUID uuid, String name, List<Payment> payments) {
        super("&3&lPayment History", 5, true);
        border("&3&lPayment History", "");

        this.player = player;
        this.uuid = uuid;
        this.name = name;
        this.payments = payments;

        this.setItem(0, 4, new GUIItem(Material.SKULL_ITEM, String.format("&3&l%s's Payment History", name), 1, "", (short)3, false, name));
        int column = 1;
        int row = 1;
        for (Payment payment : payments) {
            for (Payment.Package apackage : payment.getPackages()) {
                this.setItem(row, column, new GUIItem(apackage.getMaterial(), apackage.getName(), 1, String.format(";&r&fPayment ID: **%s**;&r&fTransaction ID: **%s**;&r&fAmount Paid: **%s GBP**;&r&fProcessed at: **%s**;&r&fStatus: %s%s", payment.getPaymentId(), payment.getTransactionId(), payment.getAmountPaid(), new Date(payment.getTimestamp()), ((payment.getStatus() == Payment.PaymentStatus.CHARGEDBACK)?"&c&lChargedBack":((payment.getStatus() == Payment.PaymentStatus.REFUNDED)?"&6&lRefunded":"&a&lProcessed")), ((payment.getCrateUUIDs().size() > 0)?";;&aClick to view attached Crates!":""))));
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
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (item.getType() == Material.STAINED_GLASS || item.getType() == Material.SKULL_ITEM) {
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
            return;
        }
        Payment payment = payments.get(((row - 1) * 7) + (column - 1));
        if (payment.getCrateUUIDs().size() > 1) {
            new BukkitRunnable(){
                @Override
                public void run() {
                    List<Crate> crates = new ArrayList<>();
                    for (UUID uuid : payment.getCrateUUIDs()) {
                        Crate crate = AuroraMCAPI.getDbManager().getCrate(uuid);
                        if (crate != null) {
                            crates.add(crate);
                        }
                    }
                    PaymentCrates paymentCrates = new PaymentCrates(player, uuid, name, payments, crates);
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            paymentCrates.open(player);
                            AuroraMCAPI.openGUI(player, paymentCrates);
                        }
                    }.runTask(AuroraMCAPI.getCore());
                }
            }.runTaskAsynchronously(AuroraMCAPI.getCore());
        } else {
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
        }
    }
}
