package net.auroramc.core.permissions.subranks;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.SubRank;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.Collections;

public class Recruitment extends SubRank {
    public Recruitment() {
        super(6, "Recruitment", new ArrayList<>(Arrays.asList(AuroraMCAPI.getPermissions().get("recruitment"), AuroraMCAPI.getPermissions().get("disguise"))), Color.fromRGB(255, 170, 0), '6');
    }
}
