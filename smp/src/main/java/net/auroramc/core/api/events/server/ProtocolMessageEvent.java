/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.events.server;

import net.auroramc.core.api.backend.communication.ProtocolMessage;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ProtocolMessageEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final ProtocolMessage message;

    public ProtocolMessageEvent(ProtocolMessage message) {
        super(true);
        this.message = message;
    }

    public ProtocolMessage getMessage() {
        return message;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }


}
