package net.auroramc.core.permissions.ranks;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.permissions.Permission;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.Collections;

public final class Elite extends Rank {


    public Elite() {
        super(1, "Elite", "Elite", "&b«ELITE»\n \n" +
                "&fThis rank is very rare, and only\n" +
                "&fthe most daring of players will\n" +
                "&fventure into its unknown value!\n\n", "http://store.auroramc.net/", 'b', 'f', 'b', 'f', false, Collections.singletonList(AuroraMCAPI.getRanks().get(0)), Collections.singletonList(Permission.ELITE), RankCategory.PLAYER, Color.fromRGB(85, 255, 255));
    }
}
