/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.api.events.player;

import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class PlayerPortalExitEvent extends PlayerTeleportEvent {

    private final Vector before;
    private Vector after;

    public PlayerPortalExitEvent(AuroraMCServerPlayer player, Location from, Location to, Vector before, Vector after, TeleportCause cause) {
        super(player, from, to, cause);
        this.before = before;
        this.after = after;
    }

    public Vector getAfter() {
        return after;
    }

    public Vector getBefore() {
        return before;
    }

    public void setAfter(Vector after) {
        this.after = after;
    }
}
