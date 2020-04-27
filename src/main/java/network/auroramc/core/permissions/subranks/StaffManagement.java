package network.auroramc.core.permissions.subranks;

import network.auroramc.core.api.permissions.SubRank;

import java.util.ArrayList;
import java.util.Collections;

public final class StaffManagement extends SubRank {
    public StaffManagement() {
        super(1, "Staff Management", new ArrayList<>(Collections.singletonList(new network.auroramc.core.permissions.permissions.StaffManagement())));
    }
}
