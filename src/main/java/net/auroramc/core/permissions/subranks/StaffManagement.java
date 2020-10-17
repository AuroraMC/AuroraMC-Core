package net.auroramc.core.permissions.subranks;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.SubRank;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.Arrays;

public final class StaffManagement extends SubRank {
    public StaffManagement() {
        super(4, "Staff Management", new ArrayList<>(Arrays.asList(AuroraMCAPI.getPermissions().get("staffmanagement"), AuroraMCAPI.getPermissions().get("disguise"))), Color.fromRGB(255, 170, 0), '6');
    }
}
