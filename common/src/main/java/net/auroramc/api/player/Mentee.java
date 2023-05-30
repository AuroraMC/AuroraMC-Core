/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.api.player;

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
