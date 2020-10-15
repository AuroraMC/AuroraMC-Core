package network.auroramc.core.permissions.subranks;

import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.permissions.Permission;
import network.auroramc.core.api.permissions.SubRank;
import org.bukkit.Color;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Recruitment extends SubRank {
    public Recruitment() {
        super(6, "Recruitment", new ArrayList<>(Arrays.asList(AuroraMCAPI.getPermissions().get("recruitment"), AuroraMCAPI.getPermissions().get("disguise"))), Color.fromRGB(255, 170, 0), '6');
    }
}
