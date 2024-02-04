/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.api.events.player;

import net.auroramc.api.cosmetics.Cosmetic;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerUseCosmeticEvent extends PlayerEvent implements Cancellable {

    private final AuroraMCServerPlayer player;
    private boolean cancelled;
    private final Cosmetic cosmetic;

    private static HandlerList handlerList = new HandlerList();


    public PlayerUseCosmeticEvent(AuroraMCServerPlayer player, Cosmetic cosmetic) {
        super(player);
        this.player = player;
        this.cosmetic = cosmetic;
    }

    public PlayerUseCosmeticEvent(AuroraMCServerPlayer player, Cosmetic cosmetic, boolean isAsync) {
        super(player, isAsync);
        this.player = player;
        this.cosmetic = cosmetic;
    }


    public AuroraMCServerPlayer getPlayer() {
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
