/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.friendstatuses;

import net.auroramc.core.api.cosmetics.FriendStatus;
import net.auroramc.core.api.permissions.Rank;

import java.util.Collections;

public class CommittedForLife extends FriendStatus {

    public CommittedForLife() {
        super(115, "&4&k0&r &cCommitted For Life &4&k0&r", "&4&k0&r &cCommitted For Life &4&k0&r", UnlockMode.RANK, -1, Collections.emptyList(), Collections.singletonList(Rank.OWNER), "", "&4&l&k0&r &c&lCommitted For Life &4&l&k0&r", 'c', false, Rarity.COMMON);
    }
}
