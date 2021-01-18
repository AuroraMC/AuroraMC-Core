package net.auroramc.core.permissions.subranks;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.SubRank;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.Collections;

public class CommunityManagement extends SubRank {

    public CommunityManagement() {
        super(9, "Community Management", new ArrayList<>(Collections.singletonList(AuroraMCAPI.getPermissions().get("cm"))), Color.fromRGB(0, 170, 0), '2');
    }
}
