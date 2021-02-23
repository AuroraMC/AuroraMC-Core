package net.auroramc.core.permissions.subranks;

import net.auroramc.core.api.permissions.SubRank;
import net.auroramc.core.permissions.Permission;
import org.bukkit.Color;

import java.util.Arrays;

public final class Recruitment extends SubRank {
    public Recruitment() {
        super(6, "Recruitment", Arrays.asList(Permission.RECRUITMENT, Permission.DISGUISE), Color.fromRGB(255, 170, 0), '6');
    }
}
