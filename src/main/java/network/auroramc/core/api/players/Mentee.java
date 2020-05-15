package network.auroramc.core.api.players;

public class Mentee {

    private final int amcId;
    private final String name;

    public Mentee(int amcId, String name) {
        this.amcId = amcId;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getAmcId() {
        return amcId;
    }
}
