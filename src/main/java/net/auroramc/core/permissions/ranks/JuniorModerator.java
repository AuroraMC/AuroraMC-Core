package net.auroramc.core.permissions.ranks;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.permissions.Permission;
import org.bukkit.Color;

import java.util.Collections;

public final class JuniorModerator extends Rank {


    public JuniorModerator() {
        super(9, "Junior Moderator", "Jr.Mod", "&9«JUNIOR MOD»\n \n" +
                        "&fJunior Mods answer any questions or\n" +
                        "&fqueries players have, as well as\n" +
                        "&fmoderate the network. Junior Mods have\n" +
                        "&fless permissions than Moderators.", null, '9', 'f', '9', 'f', false, Collections.singletonList(AuroraMCAPI.getRanks().get(2)), Collections.singletonList(Permission.MODERATION), RankCategory.MODERATION, Color.fromRGB(85, 85, 255));
    }
}
