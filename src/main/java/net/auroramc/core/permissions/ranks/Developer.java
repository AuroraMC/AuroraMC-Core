package net.auroramc.core.permissions.ranks;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.permissions.Permission;
import org.bukkit.Color;

import java.util.Collections;

public final class Developer extends Rank {


    public Developer() {
        super(12, "Developer", "Dev", "&a«DEV»\n \n" +
                        "&fDevelopers create the content that\n" +
                        "&fyou see on all our servers! They work\n" +
                        "&fbehind the scenes coding the games you\n" +
                        "&flove to play!", null, 'a', 'f', 'a', 'f', true, Collections.singletonList(AuroraMCAPI.getRanks().get(2)), Collections.singletonList(Permission.DEBUG_INFO), RankCategory.CONTENT_CREATOR, Color.fromRGB(85, 255, 85));
    }
}
