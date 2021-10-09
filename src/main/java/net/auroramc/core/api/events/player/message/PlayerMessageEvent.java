/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.events.player.message;

import net.auroramc.core.api.events.player.PlayerEvent;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.event.Cancellable;

public abstract class PlayerMessageEvent extends PlayerEvent implements Cancellable {

    private String rawMessage;
    private boolean cancelled;

    public PlayerMessageEvent(AuroraMCPlayer player, String rawMessage) {
        super(player);
        this.rawMessage = rawMessage;
        cancelled = false;
    }

    public PlayerMessageEvent(AuroraMCPlayer player, String rawMessage, boolean isAsync) {
        super(player, isAsync);
        this.rawMessage = rawMessage;
        cancelled = false;
    }

    public String getRawMessage() {
        return rawMessage;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
