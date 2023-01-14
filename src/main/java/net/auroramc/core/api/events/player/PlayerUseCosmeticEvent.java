/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.events.player;

import net.auroramc.core.api.cosmetics.Cosmetic;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerUseCosmeticEvent extends PlayerEvent implements Cancellable {

    private final AuroraMCPlayer player;
    private boolean cancelled;
    private final Cosmetic cosmetic;

    private static HandlerList handlerList = new HandlerList();


    public PlayerUseCosmeticEvent(AuroraMCPlayer player, Cosmetic cosmetic) {
        super(player);
        this.player = player;
        this.cosmetic = cosmetic;
    }

    public PlayerUseCosmeticEvent(AuroraMCPlayer player, Cosmetic cosmetic, boolean isAsync) {
        super(player, isAsync);
        this.player = player;
        this.cosmetic = cosmetic;
    }


    public AuroraMCPlayer getPlayer() {
        return player;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Cosmetic getCosmetic() {
        return cosmetic;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
