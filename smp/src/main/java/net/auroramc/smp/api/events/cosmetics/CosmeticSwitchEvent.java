/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.api.events.cosmetics;

import net.auroramc.api.cosmetics.Cosmetic;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CosmeticSwitchEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled = false;
    private final AuroraMCServerPlayer player;
    private Cosmetic cosmetic;

    public CosmeticSwitchEvent(AuroraMCServerPlayer player, Cosmetic cosmetic) {
        this.player = player;
        this.cosmetic = cosmetic;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public Cosmetic getCosmetic() {
        return cosmetic;
    }

    public void setCosmetic(Cosmetic cosmetic) {
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

    public static HandlerList getHandlerList() {return handlers;}
}
