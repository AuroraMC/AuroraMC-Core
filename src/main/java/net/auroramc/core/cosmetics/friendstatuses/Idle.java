/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.friendstatuses;

import net.auroramc.core.api.cosmetics.FriendStatus;

import java.util.Collections;

public class Idle extends FriendStatus {

    public Idle() {
        super(103, "Idle", "Idle", UnlockMode.ALL, -1, Collections.emptyList(), Collections.emptyList(), "", "Idle", 'e', true, Rarity.COMMON);
    }
}
