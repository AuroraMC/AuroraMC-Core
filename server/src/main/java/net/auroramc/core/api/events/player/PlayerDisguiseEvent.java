/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.core.api.events.player;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerDisguiseEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean isCancelled;
    private String name;
    private String skin;

    public PlayerDisguiseEvent(AuroraMCServerPlayer player, String name, String skin) {
        super(player);
        this.isCancelled = false;
        this.name = name;
        this.skin = skin;
    }

    public String getName() {
        return name;
    }

    public String getSkin() {
        return skin;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSkin(String skin) {
        this.skin = skin;
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
}
