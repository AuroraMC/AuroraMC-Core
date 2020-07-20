package network.auroramc.core.permissions.ranks;

import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.permissions.Rank;
import network.auroramc.core.permissions.permissions.Disguise;
import network.auroramc.core.permissions.permissions.SocialMedia;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public final class YouTube extends Rank {


    public YouTube() {
        super(5, "YouTube", "YouTube", "&6«YOUTUBE»\n\n" +
                        "&fThis rank is given to YouTube\n" +
                        "&fcontent creators on AuroraMC.\n \n" +
                        "&aClick to view rank requirements.", "https://forums.block2block.me/", '6', 'f', 'e', 'f', false, new ArrayList<>(Collections.singletonList(AuroraMCAPI.getRanks().get(2))), new ArrayList<>(Arrays.asList(AuroraMCAPI.getPermissions().get("disguise"), AuroraMCAPI.getPermissions().get("social"))), RankCategory.SOCIAL_MEDIA, Color.fromRGB(255, 170, 0));
    }
}
