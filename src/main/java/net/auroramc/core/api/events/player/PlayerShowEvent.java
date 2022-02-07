/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.events.player;

import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.event.HandlerList;

public class PlayerShowEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    private AuroraMCPlayer player;
    private boolean hidden;

    public PlayerShowEvent(AuroraMCPlayer player) {
        super(player);
        this.hidden = false;
        this.player = player;
    }

    public PlayerShowEvent(AuroraMCPlayer player, boolean isAsync) {
        super(player, isAsync);
        this.hidden = false;
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public AuroraMCPlayer getPlayer() {
        return player;
    }

    public void setPlayer(AuroraMCPlayer player) {
        this.player = player;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
