/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.api.events.player;

import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.meta.BookMeta;

public class PlayerEditBookEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final int slot;
    private final BookMeta previousBookMeta;
    private BookMeta newBookMeta;
    private boolean isSigning;
    private boolean cancelled;

    public PlayerEditBookEvent(AuroraMCServerPlayer player, int slot, BookMeta previousBookMeta, BookMeta newBookMeta, boolean isSigning) {
        super(player);
        this.slot = slot;
        this.previousBookMeta = previousBookMeta;
        this.newBookMeta = newBookMeta;
        this.isSigning = isSigning;
        cancelled = false;
    }

    public BookMeta getNewBookMeta() {
        return newBookMeta;
    }

    public BookMeta getPreviousBookMeta() {
        return previousBookMeta;
    }

    public boolean isSigning() {
        return isSigning;
    }

    public int getSlot() {
        return slot;
    }

    public void setNewBookMeta(BookMeta newBookMeta) {
        this.newBookMeta = newBookMeta;
    }

    public void setSigning(boolean signing) {
        isSigning = signing;
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
