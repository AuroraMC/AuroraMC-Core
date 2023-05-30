/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
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

