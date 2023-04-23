/*
 * Copyright (c) 2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.proxy.api.events;

import net.auroramc.api.player.AuroraMCPlayer;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

public class DisguiseEvent extends Event implements Cancellable {

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
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }
}
