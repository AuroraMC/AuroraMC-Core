/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.api.backend.info;

public enum ServerState {

    STARTING_UP("Starting up"),
    IDLE("Idle"),
    RELOADING_MAPS("Reloading Maps"),
    PREPARING_GAME("Preparing Game"),
    WAITING_FOR_PLAYERS("Waiting For Players"),
    STARTING("Starting Soon"),
    IN_GAME ("In-Game"),
    ENDING("Ending"),
    //Duel specific states.
    ACTIVE("Active"),
    INACTIVE("Inactive");

    private String name;

    ServerState(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
