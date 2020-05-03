package network.auroramc.core.permissions.ranks;

import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.permissions.Rank;
import network.auroramc.core.permissions.permissions.Moderation;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.Collections;

public final class TrialModerator extends Rank {


    public TrialModerator() {
        super(9, "Trial Moderator", "T.Mod", "&c&l«TRIAL MOD»\n \n" +
                        "&fTrial Mods answer any questions or\n" +
                        "&fqueries players have, as well as\n" +
                        "&fmoderate the network. Trial Mods have\n" +
                        "&fless permissions than Moderators.", null, 'c', 'f', 'c', 'f', false, new ArrayList<>(Collections.singletonList(AuroraMCAPI.getRanks().get(2))), new ArrayList<>(Collections.singletonList(AuroraMCAPI.getPermissions().get("moderation"))), RankCategory.MODERATION, Color.fromRGB(255, 85, 85));
    }
}
