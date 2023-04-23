/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.api.punishments;

import java.util.ArrayList;
import java.util.HashMap;

public class Type {

    public static final String[] TYPES = new String[]{"Chat","Game","Misc"};

    private final int type;
    private final HashMap<Integer, Weight> weights;

    public Type(int type) {
        this.type = type;
        this.weights = new HashMap<>();

        for (int i = 1;i <= 5;i++) {

            this.weights.put(i, new Weight(i));
        }
    }

    public int getType() {
        return type;
    }

    public ArrayList<Weight> getWeights() {
        return new ArrayList<>(weights.values());
    }

    public Weight getWeight(int weight) {
        return weights.get(weight);
    }

    public void registerRule(Rule rule) {
        weights.get(rule.getWeight()).registerRule(rule);
    }

}
