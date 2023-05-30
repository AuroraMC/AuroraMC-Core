/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.api.punishments;

import net.auroramc.api.AuroraMCAPI;

import java.util.ArrayList;
import java.util.HashMap;

public class PunishmentHistory {

    private final int auroramcID;
    private final HashMap<Integer, PunishmentHistoryType> history;
    private final HashMap<String, Punishment> punishments;


    public PunishmentHistory(int auroramcID) {
        this.auroramcID = auroramcID;
        this.history = new HashMap<>();

        this.punishments = new HashMap<>();

        for (int i = 1;i <= 3;i++) {
            history.put(i, new PunishmentHistoryType(i));
        }
    }

    public void registerPunishment(Punishment punishment) {
        this.history.get(AuroraMCAPI.getRules().getRule(punishment.getRuleID()).getType()).registerPunishment(punishment);
        this.punishments.put(punishment.getPunishmentCode(), punishment);
    }

    public ArrayList<Punishment> getPunishments() {
        return new ArrayList<>(punishments.values());
    }

    public Punishment getPunishment(String code) {
        return punishments.get(code);
    }

    public PunishmentHistoryType getType(int type) {
        return history.get(type);
    }

    public int getAuroramcID() {
        return auroramcID;
    }
}
