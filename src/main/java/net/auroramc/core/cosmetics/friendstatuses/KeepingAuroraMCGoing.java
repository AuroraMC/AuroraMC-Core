/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.friendstatuses;

import net.auroramc.core.api.cosmetics.FriendStatus;
import net.auroramc.core.api.permissions.Rank;

import java.util.Collections;

public class KeepingAuroraMCGoing extends FriendStatus {

    public KeepingAuroraMCGoing() {
        super(114, "&4&k0&r &cKeeping AuroraMC Going &4&k0", "&4&k0&r &cKeeping AuroraMC Going &4&k0", UnlockMode.RANK, -1, Collections.emptyList(), Collections.singletonList(Rank.ADMIN), "", "&4&l&k0&r &c&lKeeping AuroraMC Going &4&l&k0", 'c', false);
    }
}
