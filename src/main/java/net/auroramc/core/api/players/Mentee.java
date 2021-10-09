/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.players;

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
