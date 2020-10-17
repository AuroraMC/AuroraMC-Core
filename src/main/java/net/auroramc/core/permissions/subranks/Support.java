package net.auroramc.core.permissions.subranks;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.SubRank;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.Collections;

public final class Support extends SubRank {
    public Support() {
        super(5, "Support", new ArrayList<>(Collections.singletonList(AuroraMCAPI.getPermissions().get("support"))), Color.fromRGB(85, 85, 255), '9');
    }
}
