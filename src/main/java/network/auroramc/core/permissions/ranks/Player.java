package network.auroramc.core.permissions.ranks;

import network.auroramc.core.api.permissions.Rank;

import java.util.ArrayList;
import java.util.Collections;

public final class Player extends Rank {


    public Player() {
        super(0, "Player", null, null, null, null, 'f', '3', 'f', false, new ArrayList<>(), new ArrayList<>(Collections.singletonList(new network.auroramc.core.permissions.permissions.Player())), RankCategory.PLAYER);
    }
}
