/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.core.api.events.player;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.minecraft.server.level.EntityPlayer;
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
