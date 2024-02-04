/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.api.events.entity;

import net.auroramc.smp.api.player.AuroraMCServerPlayer;

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
