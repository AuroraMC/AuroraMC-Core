/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.api.events.player;

import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PlayerShootBowEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled;
    private final float force;
    private Projectile projectile;
    private final ItemStack bow;


    public PlayerShootBowEvent(AuroraMCServerPlayer player, ItemStack bow, Projectile projectile, float force) {
        super(player);
        this.cancelled = false;
        this.force = force;
        this.bow = bow;
        this.projectile = projectile;
    }

    public float getForce() {
        return force;
    }

    public ItemStack getBow() {
        return bow;
    }

    public Projectile getProjectile() {
        return projectile;
    }

    public void setProjectile(Projectile projectile) {
        this.projectile = projectile;
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

}
