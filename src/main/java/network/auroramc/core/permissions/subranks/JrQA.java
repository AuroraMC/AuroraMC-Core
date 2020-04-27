package network.auroramc.core.permissions.subranks;

import network.auroramc.core.api.permissions.SubRank;
import network.auroramc.core.permissions.permissions.DebugInfo;

import java.util.ArrayList;
import java.util.Collections;

public final class JrQA extends SubRank {
    public JrQA() {
        super(2, "Junior Quality Assurance", new ArrayList<>(Collections.singletonList(new DebugInfo())));
    }
}
