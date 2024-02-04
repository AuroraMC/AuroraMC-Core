/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.core.api.events.player;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
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
