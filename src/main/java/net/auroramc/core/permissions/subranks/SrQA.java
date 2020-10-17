package net.auroramc.core.permissions.subranks;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.SubRank;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.Arrays;

public final class SrQA extends SubRank {
    public SrQA() {
        super(3, "Senior Quality Assurance", new ArrayList<>(Arrays.asList(AuroraMCAPI.getPermissions().get("debug.info"), AuroraMCAPI.getPermissions().get("debug.action"))), Color.fromRGB(85, 255, 85), 'a');
    }
}
