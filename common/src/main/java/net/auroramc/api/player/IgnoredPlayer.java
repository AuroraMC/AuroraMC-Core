/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.api.player;

public class IgnoredPlayer {

    private final int id;
    private final String name;

    public IgnoredPlayer(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return id + "";
    }
}
