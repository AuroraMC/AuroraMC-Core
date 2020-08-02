package network.auroramc.core.api.punishments;

import org.bukkit.Bukkit;

import java.util.HashMap;

public class RuleBook {

    private final HashMap<Integer, Type> ruleBook;
    private final HashMap<Integer, Rule> rules;

    public RuleBook() {
        ruleBook = new HashMap<>();
        rules = new HashMap<>();
        for (int i = 1;i <= 3;i++) {
            ruleBook.put(i, new Type(i));
        }
    }

    public void registerRule(Rule rule) {
        rules.put(rule.getRuleID(), rule);
        ruleBook.get(rule.getType()).registerRule(rule);
    }

    public Type getType(int type) {
        return ruleBook.get(type);
    }

    public void clear() {
        rules.clear();
        ruleBook.clear();
        for (int i = 1;i <= 3;i++) {
            ruleBook.put(i, new Type(i));
        }
    }

    public Rule getRule(int id) {
        return rules.get(id);
    }

    public int noOfRules() {
        return rules.size();
    }
}
