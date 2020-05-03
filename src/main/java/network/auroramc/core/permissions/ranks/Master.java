package network.auroramc.core.permissions.ranks;

import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.permissions.Rank;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.Collections;

public final class Master extends Rank {


    public Master() {
        super(2, "Master", "Master", "&d&l«MASTER»\n \n" +
                "&fThis rank is uncommon, and is\n" +
                "&fonly found in the Realm of the\n" +
                "Unknown! It's value is incomparable!\n \n" +
                "&aClick to visit the store!", "http://store.block2block.me/", 'd', 'f', 'd', 'f', false, new ArrayList<>(Collections.singletonList(AuroraMCAPI.getRanks().get(1))), new ArrayList<>(Collections.singletonList(AuroraMCAPI.getPermissions().get("master"))), RankCategory.PLAYER, Color.fromRGB(255, 85, 255));
    }
}
