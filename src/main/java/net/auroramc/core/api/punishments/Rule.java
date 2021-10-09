/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.punishments;

public class Rule {

    private final int ruleID;
    private final String ruleName;
    private final String ruleDescription;
    private final int weight;
    private final int type;
    private final boolean active;
    private final boolean requiresWarning;

    public Rule(int ruleID, String ruleName, String ruleDescription, int weight, int type, boolean active, boolean requiresWarning) {
        this.ruleID = ruleID;
        this.ruleName = ruleName;
        this.ruleDescription = ruleDescription.replace("&#39;","'").replace("&#38;","&");
        this.weight = weight;
        this.type = type;
        this.active = active;
        this.requiresWarning = requiresWarning;
    }

    public int getRuleID() {
        return ruleID;
    }

    public int getType() {
        return type;
    }

    public int getWeight() {
        return weight;
    }

    public String getRuleDescription() {
        return ruleDescription;
    }

    public String getRuleName() {
        return ruleName;
    }

    public boolean isActive() {
        return active;
    }

    public boolean requiresWarning() {
        return requiresWarning;
    }
}
