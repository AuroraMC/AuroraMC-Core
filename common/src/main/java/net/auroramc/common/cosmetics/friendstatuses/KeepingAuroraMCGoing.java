/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.friendstatuses;

import net.auroramc.api.cosmetics.FriendStatus;
import net.auroramc.api.permissions.Rank;

import java.util.Collections;

public class KeepingAuroraMCGoing extends FriendStatus {

    public KeepingAuroraMCGoing() {
        super(114, "&4&k0&r &cKeeping AuroraMC Going &4&k0&r", "&4&k0&r &cKeeping AuroraMC Going &4&k0&r", UnlockMode.RANK, -1, Collections.emptyList(), Collections.singletonList(Rank.ADMIN), "", "&4&l&k0&r &c&lKeeping AuroraMC Going &4&l&k0&r", 'c', false, Rarity.MYTHICAL);
    }
}
