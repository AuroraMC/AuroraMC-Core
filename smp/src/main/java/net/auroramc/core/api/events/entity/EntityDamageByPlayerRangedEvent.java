/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.core.api.events.entity;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
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
