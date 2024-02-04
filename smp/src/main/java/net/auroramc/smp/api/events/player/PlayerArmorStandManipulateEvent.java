/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.api.events.player;

import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerArmorStandManipulateEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final ArmorStand armorStand;
    private boolean cancelled;

    public PlayerArmorStandManipulateEvent(AuroraMCServerPlayer player, ArmorStand armorStand) {
        super(player);
        this.armorStand = armorStand;
        cancelled = false;
    }

    public ArmorStand getArmorStand() {
        return armorStand;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}
