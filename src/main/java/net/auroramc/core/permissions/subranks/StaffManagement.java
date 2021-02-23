package net.auroramc.core.permissions.subranks;

import net.auroramc.core.api.permissions.SubRank;
import net.auroramc.core.permissions.Permission;
import org.bukkit.Color;

import java.util.Arrays;

public final class StaffManagement extends SubRank {
    public StaffManagement() {
        super(4, "Staff Management", Arrays.asList(Permission.STAFF_MANAGEMENT, Permission.DISGUISE), Color.fromRGB(255, 170, 0), '6');
    }
}
