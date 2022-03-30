/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.punishments;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.utils.TimeLength;

import java.util.ArrayList;
import java.util.HashMap;

public class PunishmentHistoryType {

    private final static int[] INITIAL_LENGTHS = new int[]{3, 24, 336, 720, -1};

    private final int type;
    private final HashMap<Integer, PunishmentHistoryWeight> weights;

    public PunishmentHistoryType(int type) {
        this.type = type;
        this.weights = new HashMap<>();

        for (int i = 1;i <= 5;i++) {

            this.weights.put(i, new PunishmentHistoryWeight(i));
        }
    }

    public void registerPunishment(Punishment punishment) {
        weights.get(AuroraMCAPI.getRules().getRule(punishment.getRuleID()).getWeight()).registerPunishment(punishment);
    }

    public ArrayList<PunishmentHistoryWeight> getWeights() {
        return new ArrayList<>(weights.values());
    }

    public PunishmentHistoryWeight getWeight(int weight) {
        return weights.get(weight);
    }

    public TimeLength generateLength(int weight) {
        double base = (INITIAL_LENGTHS[weight - 1] * (Math.pow(2, weights.get(weight).getValidPunishments().size())));
        double finalLength = base;
        if (base < 0) {
            return new TimeLength(-1);
        }
        switch (weight) {
            case 4:
                //Add on 0.1^nH to the punishment length.
                finalLength += (base * ((Math.pow(1.1, weights.get(3).getValidPunishments().size()))-1));
            case 3:
                //Add on 0.05^nM to the punishment length.
                finalLength += (base * ((Math.pow(1.05, weights.get(2).getValidPunishments().size()))-1));
            case 2:
                //Add on 0.02^nL to the punishment length.
                finalLength += (base * ((Math.pow(1.02, weights.get(1).getValidPunishments().size()))-1));
        }
        return new TimeLength(finalLength);
    }
}
