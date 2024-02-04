/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.api.backend.store;

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

        ELITE(1, "NETHER_STAR", "&b&lElite Rank"),
        MASTER(2, "EMERALD", "&d&lMaster Rank"),
        PLUS30(3, "GOLD_INGOT", "&e&lPlus Subscription (30 days)"),
        PLUS90(4, "GOLD_INGOT", "&e&lPlus Subscription (90 days)"),
        PLUS180(5, "GOLD_INGOT", "&e&lPlus Subscription (180 days)"),
        PLUS365(6, "GOLD_INGOT", "&e&lPlus Subscription (365 days)"),
        CELEBRATION(7, "ENDER_CHEST", "&c&lGrand Celebration Bundle"),
        STARTER(8, "ENDER_CHEST", "&b&lAuroraMC Starter Pack"),
        IRON5(9, "IRON_INGOT", "&7&l5x Iron Crates"),
        GOLD5(10, "GOLD_INGOT", "&6&l5x Gold Crates"),
        DIAMOND5(11, "DIAMOND", "&b&l5x Diamond Crates");

        private final int id;
        private final String material;
        private final String name;

        Package(int id,  String material, String name) {
            this.id = id;
            this.material = material;
            this.name = name;
        }

        public String getMaterial() {
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
