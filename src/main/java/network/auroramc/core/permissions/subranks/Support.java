package network.auroramc.core.permissions.subranks;

import network.auroramc.core.api.permissions.SubRank;

import java.util.ArrayList;
import java.util.Collections;

public final class Support extends SubRank {
    public Support() {
        super(2, "Support", new ArrayList<>(Collections.singletonList(new network.auroramc.core.permissions.permissions.Support())));
    }
}
