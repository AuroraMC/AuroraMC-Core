package network.auroramc.core.permissions.ranks;

import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.permissions.Rank;
import network.auroramc.core.permissions.permissions.DebugInfo;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.Collections;

public final class Developer extends Rank {


    public Developer() {
        super(12, "Developer", "Dev", "&a«DEV»\n \n" +
                        "&fDevelopers create the content that\n" +
                        "&fyou see on all our servers! They work\n" +
                        "&fbehind the scenes coding the games you\n" +
                        "&flove to play!", null, 'a', 'f', 'a', 'f', true, new ArrayList<>(Collections.singletonList(AuroraMCAPI.getRanks().get(2))), new ArrayList<>(Collections.singletonList(AuroraMCAPI.getPermissions().get("debug.info"))), RankCategory.CONTENT_CREATOR, Color.fromRGB(85, 255, 85));
    }
}
