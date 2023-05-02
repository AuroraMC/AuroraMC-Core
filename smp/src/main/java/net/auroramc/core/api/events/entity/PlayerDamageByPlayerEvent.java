/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.events.entity;

import net.auroramc.core.api.player.AuroraMCServerPlayer;

public class PlayerDamageByPlayerEvent extends PlayerDamageEvent {

    private final AuroraMCServerPlayer damager;

    public PlayerDamageByPlayerEvent(AuroraMCServerPlayer player, AuroraMCServerPlayer damager, DamageCause cause, double damage) {
        super(player, cause, damage);
        this.damager = damager;
    }

    public AuroraMCServerPlayer getDamager() {
        return damager;
    }

}
