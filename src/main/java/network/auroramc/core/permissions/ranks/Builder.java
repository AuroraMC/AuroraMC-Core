package network.auroramc.core.permissions.ranks;

import network.auroramc.core.api.permissions.Rank;

import java.util.ArrayList;
import java.util.Collections;

import static network.auroramc.core.api.permissions.Rank.RankCategory.CONTENT_CREATOR;

public final class Builder extends Rank {


    public Builder() {
        super(7, "Builder", "Builder", "&a&l«BUILDER»\n \n" +
                        "&fBuilders create and fix all of the\n" +
                        "&fmaps you can find on the network!", null, 'a', 'f', 'a', 'f', false, new ArrayList<>(Collections.singletonList(new Master())), new ArrayList<>(), CONTENT_CREATOR);
    }
}
