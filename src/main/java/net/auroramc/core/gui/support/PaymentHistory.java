/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.gui.support;

import net.auroramc.core.api.backend.store.Payment;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PaymentHistory extends GUI {
    public PaymentHistory(UUID uuid, String name, List<Payment> payments) {
        super("&3&lPayment History", 5, true);
        border("&3&lPayment History", "");

        this.setItem(0, 4, new GUIItem(Material.SKULL_ITEM, String.format("&3&l%s's Payment History", name), 1, "", (short)3, false, name));
        int column = 1;
        int row = 1;
        for (Payment payment : payments) {
            for (Payment.Package apackage : payment.getPackages()) {
                this.setItem(row, column, new GUIItem(apackage.getMaterial(), apackage.getName(), 1, String.format("&rPayment ID: **%s**;&rTransaction ID: **%s**;&rAmount Paid: **%s**;Processed at: **%s**;&rStatus: %s", payment.getPaymentId(), payment.getTransactionId(), payment.getAmountPaid(), new Date(payment.getTimestamp()), ((payment.getStatus() == Payment.PaymentStatus.CHARGEDBACK)?"&c&lChargedBack":((payment.getStatus() == Payment.PaymentStatus.REFUNDED)?"&6&lRefunded":"&a&lProcessed")))));
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

    }
}
