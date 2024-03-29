/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.core.api.events.player;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.event.HandlerList;

public class PlayerFakePlayerAttackEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    private EntityPlayer fakePlayer;

    public PlayerFakePlayerAttackEvent(AuroraMCServerPlayer player, EntityPlayer fakePlayer) {
        super(player);
        this.fakePlayer = fakePlayer;
    }

    public PlayerFakePlayerAttackEvent(AuroraMCServerPlayer player, boolean isAsync) {
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
