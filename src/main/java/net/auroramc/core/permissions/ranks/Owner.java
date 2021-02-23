package net.auroramc.core.permissions.ranks;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.permissions.Permission;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.Collections;

public final class Owner extends Rank {


    public Owner() {
        super(9001, "Owner", "Owner", "&c«OWNER»\n \n" +
                "&fOwners manage all aspects of\n" +
                "&fthe network, keeping an eye on\n" +
                "&fthe staff team and managing all\n" +
                "&fcontent that is published.", null, 'c', 'f', 'c', 'f', true, Collections.singletonList(AuroraMCAPI.getRanks().get(11)), Collections.singletonList(Permission.ALL), RankCategory.LEADERSHIP, Color.fromRGB(170, 0, 0));
    }
}
