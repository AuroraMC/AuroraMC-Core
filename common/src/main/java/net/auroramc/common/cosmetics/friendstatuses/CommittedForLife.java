/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.friendstatuses;

import net.auroramc.api.cosmetics.FriendStatus;
import net.auroramc.api.permissions.Rank;

import java.util.Collections;

public class CommittedForLife extends FriendStatus {

    public CommittedForLife() {
        super(115, "&4&k0&r &cCommitted For Life &4&k0&r", "&4&k0&r &cCommitted For Life &4&k0&r", UnlockMode.RANK, -1, Collections.emptyList(), Collections.singletonList(Rank.OWNER), "", "&4&l&k0&r &c&lCommitted For Life &4&l&k0&r", 'c', false, Rarity.MYTHICAL);
    }
}
