package net.auroramc.core.permissions.subranks;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.SubRank;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

public class EventManagement extends SubRank {

    public EventManagement() {
        super(8, "Event Management", new ArrayList<>(Collections.singletonList(AuroraMCAPI.getPermissions().get("events"))), Color.fromRGB(0, 170, 0), '2');
    }
}
