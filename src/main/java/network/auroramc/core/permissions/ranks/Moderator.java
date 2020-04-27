package network.auroramc.core.permissions.ranks;

import network.auroramc.core.api.permissions.Rank;
import network.auroramc.core.permissions.permissions.*;

import java.util.ArrayList;
import java.util.Collections;

public final class Moderator extends Rank {

    public Moderator() {
        super(10, "Moderator", "Mod", "&c&l«MOD»\n \n" +
                "&rMods answer any questions or\n" +
                "&fqueries players have, as well as\n" +
                "&fmoderate the network. ", null, 'c', 'f', 'c', 'f', false, new ArrayList<>(Collections.singletonList(new TrialModerator())), new ArrayList<>(Collections.singletonList(new BypassApproval())), RankCategory.MODERATION);
    }

}
