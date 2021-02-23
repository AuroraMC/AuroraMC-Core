package net.auroramc.core.permissions.ranks;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.permissions.Permission;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.Collections;

public final class Builder extends Rank {


    public Builder() {
        super(7, "Builder", "Builder", "&a«BUILDER»\n \n" +
                        "&fBuilders create and fix all of the\n" +
                        "&fmaps you can find on the network!", null, 'a', 'f', 'a', 'f', false, Collections.singletonList(AuroraMCAPI.getRanks().get(2)), Collections.singletonList(Permission.BUILD), RankCategory.CONTENT_CREATOR, Color.fromRGB(85, 255, 85));
    }
}
