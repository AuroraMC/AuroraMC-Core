/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.api.events.entity;

import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerCombustByBlockEvent extends PlayerCombustEvent implements Cancellable {

    private static final HandlerList handlerList = new HandlerList();

    private final Block block;

    public PlayerCombustByBlockEvent(AuroraMCServerPlayer player, int duration, Block block) {
        super(player, duration);
        this.block = block;
    }

    public Block getBlock() {
        return block;
    }

    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

}