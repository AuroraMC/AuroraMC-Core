package net.auroramc.core.api.stats;

public enum StatsGame {

    GENERAL(0);

    private int id;

    StatsGame(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
