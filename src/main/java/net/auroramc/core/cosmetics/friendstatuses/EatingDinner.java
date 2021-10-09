/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.friendstatuses;

import net.auroramc.core.api.cosmetics.FriendStatus;

import java.util.Collections;

public class EatingDinner extends FriendStatus {

    public EatingDinner() {
        super(121, "&bEating Dinner", "&bEating Dinner", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", "&b&lEating Dinner", 'b', true);
    }
}
