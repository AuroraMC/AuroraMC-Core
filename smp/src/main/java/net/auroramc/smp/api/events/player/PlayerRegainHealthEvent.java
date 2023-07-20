/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.api.events.player;

import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerRegainHealthEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled;
    private double amount;
    private final RegainReason reason;

    public PlayerRegainHealthEvent(AuroraMCServerPlayer player, double amount, RegainReason reason) {
        super(player);
        this.cancelled = false;

        this.reason = reason;
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public RegainReason getRegainReason() {
        return reason;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public static enum RegainReason {
        REGEN,
        SATIATED,
        EATING,
        ENDER_CRYSTAL,
        MAGIC,
        MAGIC_REGEN,
        WITHER_SPAWN,
        WITHER,
        CUSTOM
    }

}
