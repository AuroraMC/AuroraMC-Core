/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.api.events.player;

import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.GameMode;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerGameModeChangeEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private GameMode newGameMode;
    private boolean cancelled;

    public PlayerGameModeChangeEvent(AuroraMCServerPlayer player, GameMode newGameMode) {
        super(player);
        this.newGameMode = newGameMode;
        cancelled = false;
    }

    public GameMode getNewGameMode() {
        return newGameMode;
    }

    public void setNewGameMode(GameMode newGameMode) {
        this.newGameMode = newGameMode;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}
