package net.auroramc.core.permissions.ranks;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.Rank;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.Collections;

public final class Builder extends Rank {


    public Builder() {
        super(7, "Builder", "Builder", "&a«BUILDER»\n \n" +
                        "&fBuilders create and fix all of the\n" +
                        "&fmaps you can find on the network!", null, 'a', 'f', 'a', 'f', false, new ArrayList<>(Collections.singletonList(AuroraMCAPI.getRanks().get(2))), new ArrayList<>(Collections.singletonList(AuroraMCAPI.getPermissions().get("build"))), RankCategory.CONTENT_CREATOR, Color.fromRGB(85, 255, 85));
    }
}
