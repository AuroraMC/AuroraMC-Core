/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.core.api.events.entity;

import net.auroramc.core.api.events.player.PlayerEvent;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.PortalType;
import org.bukkit.block.BlockState;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import java.util.List;

public class PlayerCreatePortalEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlerList = new HandlerList();
    private boolean cancelled;
    private final List<BlockState> blocks;
    private final PortalType type;

    public PlayerCreatePortalEvent(AuroraMCServerPlayer player, List<BlockState> blocks, PortalType type) {
        super(player);
        cancelled = false;
        this.blocks = blocks;
        this.type = type;
    }

    public PortalType getType() {
        return type;
    }

    public List<BlockState> getBlocks() {
        return blocks;
    }

    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

}
