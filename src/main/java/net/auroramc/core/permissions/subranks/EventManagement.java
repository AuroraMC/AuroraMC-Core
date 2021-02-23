package net.auroramc.core.permissions.subranks;

import net.auroramc.core.api.permissions.SubRank;
import net.auroramc.core.permissions.Permission;
import org.bukkit.Color;

import java.util.Collections;

public final class EventManagement extends SubRank {

    public EventManagement() {
        super(8, "Event Management", Collections.singletonList(Permission.EVENT_MANAGEMENT), Color.fromRGB(0, 170, 0), '2');
    }
}
