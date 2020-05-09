package network.auroramc.core.api.punishments;

public class Rule {

    private final int ruleID;
    private final String ruleName;
    private final String ruleDescription;
    private final int weight;
    private final int type;
    private final boolean active;

    public Rule(int ruleID, String ruleName, String ruleDescription, int weight, int type, boolean active) {
        this.ruleID = ruleID;
        this.ruleName = ruleName;
        this.ruleDescription = ruleDescription;
        this.weight = weight;
        this.type = type;
        this.active = active;
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
}
