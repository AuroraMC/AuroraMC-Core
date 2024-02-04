/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.friendstatuses;

import net.auroramc.api.cosmetics.FriendStatus;

import java.util.Collections;

public class EatingDinner extends FriendStatus {

    public EatingDinner() {
        super(121, "&bEating Dinner", "&bEating Dinner", UnlockMode.STORE_PURCHASE, -1, Collections.emptyList(), Collections.emptyList(), "Purchase the AuroraMC Starter Pack at;&cstore.auroramc.net to unlock this friend status!", "&b&lEating Dinner", 'b', true, Rarity.EPIC);
    }
}
