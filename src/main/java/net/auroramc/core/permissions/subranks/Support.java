package net.auroramc.core.permissions.subranks;

import net.auroramc.core.api.permissions.SubRank;
import net.auroramc.core.permissions.Permission;
import org.bukkit.Color;

import java.util.Collections;

public final class Support extends SubRank {
    public Support() {
        super(5, "Support", Collections.singletonList(Permission.SUPPORT), Color.fromRGB(85, 85, 255), '9');
    }
}
