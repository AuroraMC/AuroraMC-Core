/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
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
