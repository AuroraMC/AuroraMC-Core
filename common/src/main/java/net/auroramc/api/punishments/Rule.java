/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.api.punishments;

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
