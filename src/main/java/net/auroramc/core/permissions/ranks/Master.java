package net.auroramc.core.permissions.ranks;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.Rank;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.Collections;

public final class Master extends Rank {


    public Master() {
        super(2, "Master", "Master", "&d«MASTER»\n \n" +
                "&fThis rank is uncommon, and is\n" +
                "&fonly found in the Realm of the\n" +
                "Unknown! It's value is incomparable!\n\n", "http://store.auroramc.block2block.me/", 'd', 'f', 'd', 'f', false, new ArrayList<>(Collections.singletonList(AuroraMCAPI.getRanks().get(1))), new ArrayList<>(Collections.singletonList(AuroraMCAPI.getPermissions().get("master"))), RankCategory.PLAYER, Color.fromRGB(255, 85, 255));
    }
}
