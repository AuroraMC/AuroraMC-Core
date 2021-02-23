package net.auroramc.core.permissions.ranks;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.permissions.Permission;
import org.bukkit.Color;

import java.util.Collections;

public final class BuildTeamManagement extends Rank {


    public BuildTeamManagement() {
        super(8, "Build Team Management", "BTM", "&a«BTM»\n \n" +
                        "&fBTM members manage and maintain the\n" +
                        "&fbuilders, build server and maps!", null, 'a', 'f', 'a', 'f', false, Collections.singletonList(AuroraMCAPI.getRanks().get(7)), Collections.singletonList(Permission.BUILD_TEAM_MANAGEMENT), RankCategory.CONTENT_CREATOR, Color.fromRGB(85, 255, 85));
    }
}
