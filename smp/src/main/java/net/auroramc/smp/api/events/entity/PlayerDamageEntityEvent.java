/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.api.events.entity;

import net.auroramc.smp.api.player.AuroraMCServerPlayer;
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
