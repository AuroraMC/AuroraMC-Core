package network.auroramc.core.permissions.ranks;

import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.permissions.Rank;
import network.auroramc.core.permissions.permissions.DebugInfo;
import network.auroramc.core.permissions.permissions.Disguise;
import network.auroramc.core.permissions.permissions.SocialMedia;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public final class Admin extends Rank {


    public Admin() {
        super(11, "Administrator", "Admin", "&4&l«ADMIN»\n \n" +
                        "&fAdmins monitor their specific teams,\n" +
                        "&fmaking sure all of the staff inside those\n" +
                        "&fteams are working efficiently and to the\n" +
                        "&fbest of their ability.", null, '4', 'f', 'c', 'f', true, new ArrayList<>(Collections.singletonList(AuroraMCAPI.getRanks().get(10))), new ArrayList<>(Arrays.asList(AuroraMCAPI.getPermissions().get("admin"), AuroraMCAPI.getPermissions().get("disguise"), AuroraMCAPI.getPermissions().get("social"), AuroraMCAPI.getPermissions().get("debug.info"))), RankCategory.LEADERSHIP, Color.fromRGB(170, 0, 0));
    }
}
