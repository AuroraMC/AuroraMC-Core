package network.auroramc.core.permissions.ranks;

import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.permissions.Rank;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.Collections;

public final class Player extends Rank {


    public Player() {
        super(0, "Player", null, null, null, null, 'f', '3', 'f', false, new ArrayList<>(), new ArrayList<>(Collections.singletonList(AuroraMCAPI.getPermissions().get("player"))), RankCategory.PLAYER, Color.fromRGB(255, 255, 255));
    }
}
