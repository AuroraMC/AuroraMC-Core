package network.auroramc.core.permissions.subranks;

import network.auroramc.core.api.permissions.SubRank;
import network.auroramc.core.permissions.permissions.Admin;
import network.auroramc.core.permissions.permissions.DebugAction;

import java.util.ArrayList;
import java.util.Arrays;

public final class SrDev extends SubRank {
    public SrDev() {
        super(2, "Senior Developer", new ArrayList<>(Arrays.asList(new DebugAction(), new Admin())));
    }
}
