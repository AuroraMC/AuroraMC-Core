/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.api.events.player;

import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.Location;

public class PlayerTeleportEvent extends PlayerMoveEvent {

    private TeleportCause cause;

    public PlayerTeleportEvent(AuroraMCServerPlayer player, Location from, Location to, TeleportCause cause) {
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
