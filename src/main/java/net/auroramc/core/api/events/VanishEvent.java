/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.events;

import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VanishEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean isCancelled;
    private final AuroraMCPlayer player;
    private boolean vanish;

    public VanishEvent(AuroraMCPlayer player, boolean vanish) {
        this.isCancelled = false;
        this.player = player;
        this.vanish = vanish;
    }

    public AuroraMCPlayer getPlayer() {
        return player;
    }

    public boolean isVanish() {
        return vanish;
    }

    public void setVanish(boolean vanish) {
        this.vanish = vanish;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
