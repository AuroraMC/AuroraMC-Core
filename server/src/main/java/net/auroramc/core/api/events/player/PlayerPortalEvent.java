/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.events.player;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.Location;
import org.bukkit.TravelAgent;

public class PlayerPortalEvent extends PlayerTeleportEvent {


    private boolean useTravelAgent;
    private TravelAgent travelAgent;

    public PlayerPortalEvent(AuroraMCServerPlayer player, Location from, Location to, TravelAgent pta, PlayerTeleportEvent.TeleportCause cause) {
        super(player, from, to, cause);
        travelAgent = pta;
        useTravelAgent = true;
    }

    public boolean useTravelAgent() {
        return useTravelAgent;
    }

    public void setTravelAgent(TravelAgent travelAgent) {
        this.travelAgent = travelAgent;
    }

    public TravelAgent getTravelAgent() {
        return travelAgent;
    }

    public void useTravelAgent(boolean useTravelAgent) {
        this.useTravelAgent = useTravelAgent;
    }
}
