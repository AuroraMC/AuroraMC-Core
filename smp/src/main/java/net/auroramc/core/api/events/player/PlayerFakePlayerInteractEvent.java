/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.events.player;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.event.HandlerList;

public class PlayerFakePlayerInteractEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    private EntityPlayer fakePlayer;

    public PlayerFakePlayerInteractEvent(AuroraMCServerPlayer player, EntityPlayer fakePlayer) {
        super(player);
        this.fakePlayer = fakePlayer;
    }

    public PlayerFakePlayerInteractEvent(AuroraMCServerPlayer player, boolean isAsync) {
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
