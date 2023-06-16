/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.api.events.player;

import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.event.HandlerList;

public class PlayerObjectCreationEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    private AuroraMCServerPlayer player;

    public PlayerObjectCreationEvent(AuroraMCServerPlayer player) {
        super(player);
        this.player = player;
    }

    public PlayerObjectCreationEvent(AuroraMCServerPlayer player, boolean isAsync) {
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
    public AuroraMCServerPlayer getPlayer() {
        return player;
    }

    public void setPlayer(AuroraMCServerPlayer player) {
        this.player = player;
    }
}
