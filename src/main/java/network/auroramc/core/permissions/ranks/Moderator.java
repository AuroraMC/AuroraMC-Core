package network.auroramc.core.permissions.ranks;

import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.permissions.Rank;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.Collections;

public final class Moderator extends Rank {

    public Moderator() {
        super(10, "Moderator", "Mod", "&9«MOD»\n \n" +
                "&rMods answer any questions or\n" +
                "&fqueries players have, as well as\n" +
                "&fmoderate the network. ", null, '9', 'f', '9', 'f', false, new ArrayList<>(Collections.singletonList(AuroraMCAPI.getRanks().get(9))), new ArrayList<>(Collections.singletonList(AuroraMCAPI.getPermissions().get("approval.bypass"))), RankCategory.MODERATION, Color.fromRGB(85, 85, 255));
    }

}
