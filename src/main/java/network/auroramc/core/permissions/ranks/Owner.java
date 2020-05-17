package network.auroramc.core.permissions.ranks;

import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.permissions.Rank;
import network.auroramc.core.permissions.permissions.All;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.Collections;

public final class Owner extends Rank {


    public Owner() {
        super(9001, "Owner", "Owner", "&c«OWNER»\n \n" +
                "&fOwners manage all aspects of\n" +
                "&fthe network, keeping an eye on\n" +
                "&fthe staff team and managing all\n" +
                "&fcontent that is published.", "", 'c', 'f', 'c', 'f', true, new ArrayList<>(Collections.singletonList(AuroraMCAPI.getRanks().get(11))), new ArrayList<>(Collections.singletonList(AuroraMCAPI.getPermissions().get("all"))), RankCategory.LEADERSHIP, Color.fromRGB(170, 0, 0));
    }
}
