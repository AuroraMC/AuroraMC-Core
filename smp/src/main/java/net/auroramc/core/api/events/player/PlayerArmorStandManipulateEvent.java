/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.events.player;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
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
