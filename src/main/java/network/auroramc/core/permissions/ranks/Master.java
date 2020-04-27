package network.auroramc.core.permissions.ranks;

import network.auroramc.core.api.permissions.Rank;

import java.util.ArrayList;
import java.util.Collections;

public final class Master extends Rank {


    public Master() {
        super(2, "Master", "Master", "&b&l«MASTER»\n \n" +
                "&fThis rank is uncommon, and is\n" +
                "&fonly found in the Realm of the\n" +
                "Unknown! It's value is incomparable!\n \n" +
                "&aClick to visit the store!", "http://store.block2block.me/", 'b', 'f', 'b', 'f', false, new ArrayList<>(Collections.singletonList(new Elite())), new ArrayList<>(Collections.singletonList(new network.auroramc.core.permissions.permissions.Master())), RankCategory.PLAYER);
    }
}
