/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.events;

import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DisguiseEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean isCancelled;
    private AuroraMCPlayer player;
    private String name;
    private String skin;

    public DisguiseEvent(AuroraMCPlayer player, String name, String skin) {
        this.isCancelled = false;
        this.player = player;
        this.name = name;
        this.skin = skin;
    }

    public AuroraMCPlayer getPlayer() {
        return player;
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
