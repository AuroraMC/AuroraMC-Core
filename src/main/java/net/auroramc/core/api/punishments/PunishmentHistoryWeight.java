/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.punishments;

import java.util.ArrayList;
import java.util.HashMap;

public class PunishmentHistoryWeight {

    private final int weight;
    private final HashMap<String, Punishment> punishments;

    public PunishmentHistoryWeight(int weight) {
        this.weight = weight;
        this.punishments = new HashMap<>();
    }

    public void registerPunishment(Punishment punishment) {
        punishments.put(punishment.getPunishmentCode(), punishment);
    }

    public int getWeight() {
        return weight;
    }

    public ArrayList<Punishment> getPunishments() {
        return new ArrayList<>(punishments.values());
    }

    public ArrayList<Punishment> getValidPunishments() {
        ArrayList<Punishment> validPunishments = new ArrayList<>();
        for (Punishment punishment : punishments.values()) {
            if (punishment.getStatus() != 4 && punishment.getStatus() != 7 && punishment.getRemovalReason() == null) {
                validPunishments.add(punishment);
            }
        }
        return validPunishments;
    }

    public Punishment getPunishment(String code) {
        return punishments.get(code);
    }

    public boolean issueWarning(Rule rule) {
        for (Punishment punishment : punishments.values()) {
            if (punishment.getRuleID() == rule.getRuleID()) {
                if (System.currentTimeMillis() - punishment.getIssued() < 2592000000L) {
                    //There has been a punishment issued in the last 30 days for this rule, does not require warning.
                    return false;
                }
            }
        }
        return true;
    }
}
