package net.auroramc.core.permissions.subranks;

import net.auroramc.core.api.permissions.SubRank;
import net.auroramc.core.permissions.Permission;
import org.bukkit.Color;

import java.util.Arrays;

public final class SrDev extends SubRank {
    public SrDev() {
        super(1, "Senior Developer", Arrays.asList(Permission.DEBUG_ACTION, Permission.ADMIN), Color.fromRGB(85, 255, 85), 'a');
    }
}
