/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.events.entity;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.entity.Entity;

public class EntityDamageByPlayerEvent extends PlayerDamageEvent {

    private final Entity damaged;

    public EntityDamageByPlayerEvent(AuroraMCServerPlayer player, Entity damaged, DamageCause cause, double damage) {
        super(player, cause, damage);
        this.damaged = damaged;
    }

    public Entity getDamaged() {
        return damaged;
    }
}
