/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.api.events.entity;

import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.entity.Entity;

public class PlayerDamageByEntityEvent extends PlayerDamageEvent {

    private final Entity damager;

    public PlayerDamageByEntityEvent(AuroraMCServerPlayer player, Entity entity, DamageCause cause, double damage) {
        super(player, cause, damage);
        this.damager = entity;
    }

    public Entity getDamager() {
        return damager;
    }
}
