package net.auroramc.core.permissions.ranks;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.permissions.Permission;
import org.bukkit.Color;

import java.util.Arrays;
import java.util.Collections;

public final class YouTube extends Rank {


    public YouTube() {
        super(5, "YouTube", "YouTube", "&6«YOUTUBE»\n\n" +
                        "&fThis rank is given to YouTube\n" +
                        "&fcontent creators on AuroraMC.\n \n" +
                        "&aClick to view rank requirements.", "https://auroramc.net/threads/content-creator-ranks-information.49/", '6', 'f', 'e', 'f', false, Collections.singletonList(AuroraMCAPI.getRanks().get(2)), Arrays.asList(Permission.DISGUISE, Permission.SOCIAL), RankCategory.SOCIAL_MEDIA, Color.fromRGB(255, 170, 0));
    }
}
