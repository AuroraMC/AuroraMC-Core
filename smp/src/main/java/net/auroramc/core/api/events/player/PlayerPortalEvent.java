/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.events.player;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.minecraft.world.level.portal.PortalTravelAgent;
import org.bukkit.Location;

public class PlayerPortalEvent extends PlayerTeleportEvent {


    private boolean useTravelAgent;
    private PortalTravelAgent travelAgent;

    public PlayerPortalEvent(AuroraMCServerPlayer player, Location from, Location to, PortalTravelAgent pta, TeleportCause cause) {
        super(player, from, to, cause);
        travelAgent = pta;
        useTravelAgent = true;
    }

    public boolean useTravelAgent() {
        return useTravelAgent;
    }

    public void setTravelAgent(PortalTravelAgent travelAgent) {
        this.travelAgent = travelAgent;
    }

    public PortalTravelAgent getTravelAgent() {
        return travelAgent;
    }

    public void useTravelAgent(boolean useTravelAgent) {
        this.useTravelAgent = useTravelAgent;
    }
}
