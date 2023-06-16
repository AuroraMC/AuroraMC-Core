/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.api.events.player;

import net.auroramc.smp.api.player.AuroraMCServerPlayer;
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
