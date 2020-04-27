package network.auroramc.core.permissions.subranks;

import network.auroramc.core.api.permissions.SubRank;
import network.auroramc.core.permissions.permissions.DebugAction;
import network.auroramc.core.permissions.permissions.DebugInfo;

import java.util.ArrayList;
import java.util.Arrays;

public final class SrQA extends SubRank {
    public SrQA() {
        super(2, "Senior Quality Assurance", new ArrayList<>(Arrays.asList(new DebugInfo(), new DebugAction())));
    }
}
