/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.friendstatuses;

import net.auroramc.api.cosmetics.FriendStatus;

import java.util.Collections;

public class Offline extends FriendStatus {

    public Offline() {
        super(101, "Offline", "Online", UnlockMode.ALL, -1, Collections.emptyList(), Collections.emptyList(), "", "Offline", '7', true, Rarity.COMMON);
    }
}
