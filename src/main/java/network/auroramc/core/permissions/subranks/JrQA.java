package network.auroramc.core.permissions.subranks;

import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.permissions.SubRank;
import network.auroramc.core.permissions.permissions.DebugInfo;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.Collections;

public final class JrQA extends SubRank {
    public JrQA() {
        super(2, "Junior Quality Assurance", new ArrayList<>(Collections.singletonList(AuroraMCAPI.getPermissions().get("debug.info"))), Color.fromRGB(85, 255, 85), 'a');
    }
}
