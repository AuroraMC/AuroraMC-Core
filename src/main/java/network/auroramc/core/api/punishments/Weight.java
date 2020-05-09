package network.auroramc.core.api.punishments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Weight {

    public static final String[] WEIGHTS = new String[]{"&2&lLight","&a&lMedium","&e&lHeavy","&6&lSevere","&4&lExtreme"};
    public static final short[] WEIGHT_ICON_DATA = new short[]{13,5,4,1,14};

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
