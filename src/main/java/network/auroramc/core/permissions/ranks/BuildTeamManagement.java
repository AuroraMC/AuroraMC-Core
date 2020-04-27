package network.auroramc.core.permissions.ranks;

import network.auroramc.core.api.permissions.Rank;

import java.util.ArrayList;
import java.util.Collections;

public final class BuildTeamManagement extends Rank {


    public BuildTeamManagement() {
        super(8, "Build Team Management", "BTM", "&a&l«BTM»\n \n" +
                        "&fBTM members manage and maintain the\n" +
                        "&fbuilders, build server and maps!", null, 'a', 'f', 'a', 'f', false, new ArrayList<>(Collections.singletonList(new Builder())), new ArrayList<>(Collections.singletonList(new network.auroramc.core.permissions.permissions.BuildTeamManagement())), RankCategory.CONTENT_CREATOR);
    }
}
