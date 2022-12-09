/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.events;

import net.auroramc.core.api.events.player.PlayerEvent;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.event.HandlerList;

public class FakePlayerInteractEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    private EntityPlayer fakePlayer;

    public FakePlayerInteractEvent(AuroraMCPlayer player, EntityPlayer fakePlayer) {
        super(player);
        this.fakePlayer = fakePlayer;
    }

    public FakePlayerInteractEvent(AuroraMCPlayer player, boolean isAsync) {
        super(player, isAsync);
    }

    public EntityPlayer getFakePlayer() {
        return fakePlayer;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
