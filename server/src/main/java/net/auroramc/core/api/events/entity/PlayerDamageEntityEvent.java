/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.core.api.events.entity;

import com.google.common.base.Function;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import java.util.Map;

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
