/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
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
