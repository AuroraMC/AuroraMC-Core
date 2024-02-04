/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.api.events.entity;

import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;

public class EntityDamageByPlayerRangedEvent extends EntityDamageByPlayerEvent {

    private final Projectile projectile;

    public EntityDamageByPlayerRangedEvent(AuroraMCServerPlayer player, Entity damaged, DamageCause cause, double damage, Projectile projectile) {
        super(player, damaged, cause, damage);
        this.projectile = projectile;
    }

    public Projectile getProjectile() {
        return projectile;
    }
}
