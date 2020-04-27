package network.auroramc.core.permissions.ranks;

import network.auroramc.core.api.permissions.Rank;
import network.auroramc.core.permissions.permissions.Moderation;

import java.util.ArrayList;
import java.util.Collections;

public final class TrialModerator extends Rank {


    public TrialModerator() {
        super(9, "Trial Moderator", "T.Mod", "&c&l«TRIAL MOD»\n \n" +
                        "&fTrial Mods answer any questions or\n" +
                        "&fqueries players have, as well as\n" +
                        "&fmoderate the network. Trial Mods have\n" +
                        "&fless permissions than Moderators.", null, 'c', 'f', 'c', 'f', false, new ArrayList<>(Collections.singletonList(new Master())), new ArrayList<>(Collections.singletonList(new Moderation())), RankCategory.MODERATION);
    }
}
