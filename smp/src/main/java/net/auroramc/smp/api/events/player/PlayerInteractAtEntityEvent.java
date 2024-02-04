/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.api.events.player;

import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.entity.Entity;
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
