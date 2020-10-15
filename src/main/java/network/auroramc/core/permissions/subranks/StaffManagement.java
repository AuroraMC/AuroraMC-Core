package network.auroramc.core.permissions.subranks;

import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.permissions.SubRank;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public final class StaffManagement extends SubRank {
    public StaffManagement() {
        super(4, "Staff Management", new ArrayList<>(Arrays.asList(AuroraMCAPI.getPermissions().get("staffmanagement"), AuroraMCAPI.getPermissions().get("disguise"))), Color.fromRGB(255, 170, 0), '6');
    }
}
