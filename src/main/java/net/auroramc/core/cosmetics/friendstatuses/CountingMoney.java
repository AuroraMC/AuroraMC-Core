/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.friendstatuses;

import net.auroramc.core.api.cosmetics.FriendStatus;
import net.auroramc.core.api.permissions.Rank;

import java.util.Collections;

public class CountingMoney extends FriendStatus {

    public CountingMoney() {
        super(105, "Counting Money", "Counting Money", UnlockMode.RANK, -1, Collections.emptyList(), Collections.singletonList(Rank.ELITE), "Purchase Elite at store.auroramc.net to unlock this status.", "Counting Money", 'b', true, Rarity.COMMON);
    }
}
