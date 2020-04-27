package network.auroramc.core.permissions.ranks;

import network.auroramc.core.api.permissions.Rank;

import java.util.ArrayList;
import java.util.Collections;

public final class Elite extends Rank {


    public Elite() {
        super(1, "Elite", "Elite", "&d&l«ELITE»\n \n" +
                "&fThis rank is very rare, and only\n" +
                "&fthe most daring of players will\n" +
                "&fventure into its unknown value!\n \n" +
                "&aClick to visit the store!", "http://store.block2block.me/", 'd', 'f', 'd', 'f', false, new ArrayList<>(Collections.singletonList(new Player())), new ArrayList<>(Collections.singletonList(new network.auroramc.core.permissions.permissions.Elite())), RankCategory.PLAYER);
    }
}
