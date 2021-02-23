package net.auroramc.core.permissions.subranks;

import net.auroramc.core.api.permissions.SubRank;
import org.bukkit.Color;

import java.util.Collections;

public final class CommunityManagement extends SubRank {

    public CommunityManagement() {
        super(9, "Community Management", Collections.emptyList(), Color.fromRGB(0, 170, 0), '2');
    }
}
