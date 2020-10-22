package net.auroramc.core.permissions.ranks;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.Rank;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public final class Twitch extends Rank {


    public Twitch() {
        super(6, "Twitch", "Twitch", "&5«TWITCH»\n\n" +
                        "&fThis rank is given to Twitch\n" +
                        "&fstreamers on AuroraMC.\n \n" +
                        "&aClick to view rank requirements.", "https://auroramc.net/threads/content-creator-ranks-information.49/", '5', 'f', 'd', 'f', false, new ArrayList<>(Collections.singletonList(AuroraMCAPI.getRanks().get(2))), new ArrayList<>(Arrays.asList(AuroraMCAPI.getPermissions().get("disguise"), AuroraMCAPI.getPermissions().get("social"))), RankCategory.SOCIAL_MEDIA, Color.fromRGB(170, 0, 170));
    }
}
