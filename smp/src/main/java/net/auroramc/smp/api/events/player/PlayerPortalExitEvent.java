/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
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
