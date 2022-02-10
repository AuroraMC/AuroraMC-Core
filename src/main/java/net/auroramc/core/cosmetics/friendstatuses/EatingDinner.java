/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.friendstatuses;

import net.auroramc.core.api.cosmetics.FriendStatus;

import java.util.Collections;

public class EatingDinner extends FriendStatus {

    public EatingDinner() {
        super(121, "&bEating Dinner", "&bEating Dinner", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Purchase the AuroraMC Starter Pack at;&cstore.auroramc.net to unlock this friend status!", "&b&lEating Dinner", 'b', true, Rarity.COMMON);
    }
}
