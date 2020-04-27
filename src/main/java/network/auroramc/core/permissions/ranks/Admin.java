package network.auroramc.core.permissions.ranks;

import network.auroramc.core.api.permissions.Rank;
import network.auroramc.core.permissions.permissions.DebugInfo;
import network.auroramc.core.permissions.permissions.Disguise;
import network.auroramc.core.permissions.permissions.SocialMedia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public final class Admin extends Rank {


    public Admin() {
        super(11, "Administrator", "Admin", "&4&l«ADMIN»\n \n" +
                        "&fAdmins monitor their specific teams,\n" +
                        "&fmaking sure all of the staff inside those\n" +
                        "&fteams are working efficiently and to the\n" +
                        "&fbest of their ability.", null, '4', 'f', 'c', 'f', true, new ArrayList<>(Collections.singletonList(new Moderator())), new ArrayList<>(Arrays.asList(new network.auroramc.core.permissions.permissions.Admin(), new Disguise(), new SocialMedia(), new DebugInfo())), RankCategory.LEADERSHIP);
    }
}
