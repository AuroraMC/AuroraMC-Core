/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.core.api.events.entity;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
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
