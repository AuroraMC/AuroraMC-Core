/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.api.stats;

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
