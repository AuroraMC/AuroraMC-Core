/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.backend.store;

import net.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class Payment {

    private final int paymentId;
    private final int amcId;
    private final String transactionId;
    private final double amountPaid;
    private final long timestamp;
    private final List<Package> packages;
    private final List<UUID> crateUUIDs;
    private final PaymentStatus status;

    public Payment(int paymentId, int amcId, String transactionId, double amountPaid, long timestamp, List<Package> packages, List<UUID> crateUUIDs, PaymentStatus status) {
        this.amcId = amcId;
        this.packages = packages;
        this.paymentId = paymentId;
        this.transactionId = transactionId;
        this.amountPaid = amountPaid;
        this.timestamp = timestamp;
        this.crateUUIDs = crateUUIDs;
        this.status = status;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public int getAmcId() {
        return amcId;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public List<Package> getPackages() {
        return packages;
    }

    public List<UUID> getCrateUUIDs() {
        return crateUUIDs;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public enum PaymentStatus{PROCESSED, REFUNDED, CHARGEDBACK}

    public enum Package {

        ELITE(1, Material.NETHER_STAR, "&b&lElite Rank"),
        MASTER(2, Material.EMERALD, "&d&lMaster Rank"),
        PLUS30(3, Material.GOLD_INGOT, "&e&lPlus Subscription (30 days)"),
        PLUS90(4, Material.GOLD_INGOT, "&e&lPlus Subscription (90 days)"),
        PLUS180(5, Material.GOLD_INGOT, "&e&lPlus Subscription (180 days)"),
        PLUS365(6, Material.GOLD_INGOT, "&e&lPlus Subscription (365 days)"),
        CELEBRATION(7, Material.ENDER_CHEST, "&c&lGrand Celebration Bundle"),
        STARTER(8, Material.ENDER_CHEST, "&b&lAuroraMC Starter Pack"),
        IRON5(9, Material.IRON_INGOT, "&7&l5x Iron Crates"),
        GOLD5(10, Material.GOLD_INGOT, "&6&l5x Gold Crates"),
        DIAMOND5(11, Material.DIAMOND, "&b&l5x Diamond Crates");

        private final int id;
        private final Material material;
        private final String name;

        Package(int id, Material material, String name) {
            this.id = id;
            this.material = material;
            this.name = name;
        }

        public Material getMaterial() {
            return material;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        public static Package getByID(int id) {
            for (Package aPackage: Package.values()) {
                if (aPackage.id == id) {
                    return aPackage;
                }
            }
            return null;
        }
    }
}
