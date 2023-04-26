/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.events.player;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;

public class PlayerInteractAtEntityEvent extends PlayerInteractEntityEvent {

    private final Vector position;

    public PlayerInteractAtEntityEvent(AuroraMCServerPlayer player, Entity clickedEntity, Vector position) {
        super(player, clickedEntity);
        this.position = position;
    }

    public Vector getPosition() {
        return position;
    }
}
