/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
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
