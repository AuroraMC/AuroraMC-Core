/*
 * Copyright (c) 2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.proxy.api.events;

import net.auroramc.proxy.api.backend.communication.ProtocolMessage;
import net.md_5.bungee.api.plugin.Event;

public class ProtocolMessageEvent extends Event {

    private final ProtocolMessage message;

    public ProtocolMessageEvent(ProtocolMessage message) {
        this.message = message;
    }

    public ProtocolMessage getMessage() {
        return message;
    }

}

