/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.core.api.events.entity;

import com.google.common.base.Function;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import java.util.Map;

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
