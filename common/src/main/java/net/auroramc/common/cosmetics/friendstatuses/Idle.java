/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.friendstatuses;

import net.auroramc.api.cosmetics.FriendStatus;

import java.util.Collections;

public class Idle extends FriendStatus {

    public Idle() {
        super(103, "Idle", "Idle", UnlockMode.ALL, -1, Collections.emptyList(), Collections.emptyList(), "", "Idle", 'e', true, Rarity.COMMON);
    }
}
