/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.events.player;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.event.HandlerList;

public class PlayerLeaveEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    public PlayerLeaveEvent(AuroraMCServerPlayer player) {
        super(player);
    }

    public PlayerLeaveEvent(AuroraMCServerPlayer player, boolean isAsync) {
        super(player, isAsync);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
