package net.auroramc.core.permissions.ranks;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.permissions.Permission;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.Collections;

public final class Moderator extends Rank {

    public Moderator() {
        super(10, "Moderator", "Mod", "&9«MOD»\n \n" +
                "&rMods answer any questions or\n" +
                "&fqueries players have, as well as\n" +
                "&fmoderate the network. ", null, '9', 'f', '9', 'f', false, Collections.singletonList(AuroraMCAPI.getRanks().get(9)), Collections.singletonList(Permission.BYPASS_APPROVAL), RankCategory.MODERATION, Color.fromRGB(85, 85, 255));
    }

}
