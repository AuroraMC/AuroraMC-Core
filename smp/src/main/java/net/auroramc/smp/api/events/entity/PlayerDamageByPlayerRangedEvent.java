/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.api.events.entity;

import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.entity.Projectile;

public class PlayerDamageByPlayerRangedEvent extends PlayerDamageByPlayerEvent {

    private final Projectile projectile;

    public PlayerDamageByPlayerRangedEvent(AuroraMCServerPlayer player, AuroraMCServerPlayer damager, DamageCause cause, double damage, Projectile projectile) {
        super(player, damager, cause, damage);
        this.projectile = projectile;
    }

    public Projectile getProjectile() {
        return projectile;
    }
}