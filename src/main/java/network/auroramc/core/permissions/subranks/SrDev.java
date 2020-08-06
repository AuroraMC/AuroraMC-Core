package network.auroramc.core.permissions.subranks;

import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.permissions.SubRank;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.Arrays;

public final class SrDev extends SubRank {
    public SrDev() {
        super(1, "Senior Developer", new ArrayList<>(Arrays.asList(AuroraMCAPI.getPermissions().get("debug.action"), AuroraMCAPI.getPermissions().get("admin"))), Color.fromRGB(85, 255, 85), 'a');
    }
}
