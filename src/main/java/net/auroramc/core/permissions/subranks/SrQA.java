package net.auroramc.core.permissions.subranks;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.SubRank;
import net.auroramc.core.permissions.Permission;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.Arrays;

public final class SrQA extends SubRank {
    public SrQA() {
        super(3, "Senior Quality Assurance", Arrays.asList(Permission.DEBUG_INFO, Permission.DEBUG_ACTION), Color.fromRGB(85, 255, 85), 'a');
    }
}
