package net.auroramc.core.permissions.ranks;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.permissions.Permission;
import org.bukkit.Color;

import java.util.Collections;

public final class Master extends Rank {


    public Master() {
        super(2, "Master", "Master", "&d«MASTER»\n \n" +
                "&fThis rank is uncommon, and is\n" +
                "&fonly found in the Realm of the\n" +
                "Unknown! It's value is incomparable!\n\n", "http://store.auroramc.net/", 'd', 'f', 'd', 'f', false, Collections.singletonList(AuroraMCAPI.getRanks().get(1)), Collections.singletonList(Permission.MASTER), RankCategory.PLAYER, Color.fromRGB(255, 85, 255));
    }
}
