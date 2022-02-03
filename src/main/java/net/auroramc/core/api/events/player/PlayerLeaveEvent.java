/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.events.player;

import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.event.HandlerList;

public class PlayerLeaveEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    private final AuroraMCPlayer player;

    public PlayerLeaveEvent(AuroraMCPlayer player) {
        super(player);
        this.player = player;
    }

    public PlayerLeaveEvent(AuroraMCPlayer player, boolean isAsync) {
        super(player, isAsync);
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

}
