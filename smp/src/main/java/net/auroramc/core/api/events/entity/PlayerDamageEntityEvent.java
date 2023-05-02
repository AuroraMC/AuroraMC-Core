/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.events.entity;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.entity.Entity;

public class PlayerDamageEntityEvent extends PlayerDamageEvent {

    private final Entity damaged;

    public PlayerDamageEntityEvent(AuroraMCServerPlayer player, Entity entity, DamageCause cause, double damage) {
        super(player, cause, damage);
        this.damaged = entity;
    }

    public Entity getDamaged() {
        return damaged;
    }
}
