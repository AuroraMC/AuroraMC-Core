/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.api.punishments;

import java.util.ArrayList;
import java.util.HashMap;

public class Weight {

    public static final String[] WEIGHTS = new String[]{"&2&lLight","&a&lMedium","&e&lHeavy","&6&lSevere","&4&lExtreme"};
    public static final short[] WEIGHT_ICON_DATA = new short[]{13,5,4,1,14};
    public static final String[] WEIGHT_ICON_MATERIAL = new String[]{"GREEN_WOOL","LIME_WOOL","YELLOW_WOOL","ORANGE_WOOL","RED_WOOL"};

    private final int weight;
    private final HashMap<Integer, Rule> rules;

    public Weight(int weight) {
        this.weight = weight;
        this.rules = new HashMap<>();


    }

    public int getWeight() {
        return weight;
    }

    public ArrayList<Rule> getRules() {
        return new ArrayList<>(rules.values());
    }

    public Rule getRule(int ruleID) {
        return rules.get(ruleID);
    }

    public void registerRule(Rule rule){
        rules.put(rule.getRuleID(), rule);
    }
}
