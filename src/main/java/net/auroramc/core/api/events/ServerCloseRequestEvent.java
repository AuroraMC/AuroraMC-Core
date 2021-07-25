package net.auroramc.core.api.events;

import net.auroramc.core.api.backend.communication.ProtocolMessage;
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


}
