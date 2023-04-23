/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.friendstatuses;

import net.auroramc.api.cosmetics.FriendStatus;
import net.auroramc.api.permissions.Rank;

import java.util.Collections;

public class CreatingNewMaps extends FriendStatus {

    public CreatingNewMaps() {
        super(109, "Creating new maps", "Creating new maps", UnlockMode.RANK, -1, Collections.emptyList(), Collections.singletonList(Rank.BUILDER), "", "Creating new maps", 'a', false, Rarity.MYTHICAL);
    }
}
