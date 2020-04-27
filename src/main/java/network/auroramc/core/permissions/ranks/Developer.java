package network.auroramc.core.permissions.ranks;

import network.auroramc.core.api.permissions.Rank;
import network.auroramc.core.permissions.permissions.DebugInfo;

import java.util.ArrayList;
import java.util.Collections;

public final class Developer extends Rank {


    public Developer() {
        super(12, "Developer", "Dev", "&a&l«DEV»\n \n" +
                        "&fDevelopers create the content that\n" +
                        "&fyou see on all our servers! They work\n" +
                        "&fbehind the scenes coding the games you\n" +
                        "&flove to play!", null, 'a', 'f', 'a', 'f', true, new ArrayList<>(Collections.singletonList(new Master())), new ArrayList<>(Collections.singletonList(new DebugInfo())), RankCategory.CONTENT_CREATOR);
    }
}
