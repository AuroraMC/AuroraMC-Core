/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.core.api.events.player;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.Location;

public class PlayerTeleportEvent extends PlayerMoveEvent {

    private TeleportCause cause;

    public PlayerTeleportEvent(AuroraMCServerPlayer player, Location from, Location to, PlayerTeleportEvent.TeleportCause cause) {
        super(player, from, to);
        this.cause = cause;
    }

    public TeleportCause getCause() {
        return cause;
    }


    public static enum TeleportCause {
        ENDER_PEARL,
        COMMAND,
        PLUGIN,
        NETHER_PORTAL,
        END_PORTAL,
        SPECTATE,
        UNKNOWN
    }

}
