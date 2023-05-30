/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
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
