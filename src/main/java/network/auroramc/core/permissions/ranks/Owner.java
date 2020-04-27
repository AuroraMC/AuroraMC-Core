package network.auroramc.core.permissions.ranks;

import network.auroramc.core.api.permissions.Rank;
import network.auroramc.core.permissions.permissions.All;

import java.util.ArrayList;
import java.util.Collections;

public final class Owner extends Rank {


    public Owner() {
        super(9001, "Owner", "Owner", "", "&4&l«OWNER»\n \n" +
                        "&fOwners manage all aspects of\n" +
                        "&fthe network, keeping an eye on\n" +
                        "&fthe staff team and managing all\n" +
                        "&fcontent that is published.", '4', 'f', 'c', 'f', true, new ArrayList<>(Collections.singletonList(new Admin())), new ArrayList<>(Collections.singletonList(new All())), RankCategory.LEADERSHIP);
    }
}
