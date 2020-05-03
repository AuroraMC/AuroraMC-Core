package network.auroramc.core.permissions.subranks;

import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.permissions.SubRank;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.Collections;

public final class StaffManagement extends SubRank {
    public StaffManagement() {
        super(4, "Staff Management", new ArrayList<>(Collections.singletonList(AuroraMCAPI.getPermissions().get("staffmanagement"))), Color.fromRGB(255, 170, 0), '6');
    }
}
