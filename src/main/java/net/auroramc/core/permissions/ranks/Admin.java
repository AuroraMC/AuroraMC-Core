package net.auroramc.core.permissions.ranks;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.Rank;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public final class Admin extends Rank {

    public Admin() {
        super(11, "Administrator", "Admin", "&c«ADMIN»\n \n" +
                        "&fAdmins monitor their specific teams,\n" +
                        "&fmaking sure all of the staff inside those\n" +
                        "&fteams are working efficiently and to the\n" +
                        "&fbest of their ability.", null, 'c', 'f', 'c', 'f', true, new ArrayList<>(Collections.singletonList(AuroraMCAPI.getRanks().get(10))), new ArrayList<>(Arrays.asList(AuroraMCAPI.getPermissions().get("admin"), AuroraMCAPI.getPermissions().get("disguise"), AuroraMCAPI.getPermissions().get("social"), AuroraMCAPI.getPermissions().get("debug.info"))), RankCategory.LEADERSHIP, Color.fromRGB(170, 0, 0));
    }
}
