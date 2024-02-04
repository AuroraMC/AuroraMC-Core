/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.core.api.events.entity;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
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
