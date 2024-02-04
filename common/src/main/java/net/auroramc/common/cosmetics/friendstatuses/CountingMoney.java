/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.friendstatuses;

import net.auroramc.api.cosmetics.FriendStatus;
import net.auroramc.api.permissions.Rank;

import java.util.Collections;

public class CountingMoney extends FriendStatus {

    public CountingMoney() {
        super(105, "Counting Money", "Counting Money", UnlockMode.RANK, -1, Collections.emptyList(), Collections.singletonList(Rank.ELITE), "Purchase Elite at store.auroramc.net to unlock this status.", "Counting Money", 'b', true, Rarity.UNCOMMON);
    }
}
