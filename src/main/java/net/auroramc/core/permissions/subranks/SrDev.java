package net.auroramc.core.permissions.subranks;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.SubRank;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.Arrays;

public final class SrDev extends SubRank {
    public SrDev() {
        super(1, "Senior Developer", new ArrayList<>(Arrays.asList(AuroraMCAPI.getPermissions().get("debug.action"), AuroraMCAPI.getPermissions().get("admin"))), Color.fromRGB(85, 255, 85), 'a');
    }
}
