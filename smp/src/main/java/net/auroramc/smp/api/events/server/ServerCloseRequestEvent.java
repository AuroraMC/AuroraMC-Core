/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.api.events.server;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ServerCloseRequestEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final boolean emergency;
    private final String type;

    public ServerCloseRequestEvent(boolean emergency, String type) {
        super(true);
        this.emergency = emergency;
        this.type = type;
    }

    public boolean isEmergency() {
        return emergency;
    }

    public String getType() {
        return type;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {return handlers;}


}
