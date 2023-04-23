/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.api.punishments;

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
