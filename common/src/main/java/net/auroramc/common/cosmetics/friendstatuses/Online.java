/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.friendstatuses;

import net.auroramc.api.cosmetics.FriendStatus;

import java.util.Collections;

public class Online extends FriendStatus {

    public Online() {
        super(100, "Online", "Online", UnlockMode.ALL, -1, Collections.emptyList(), Collections.emptyList(), "", "Online", 'a', true, Rarity.COMMON);
    }
}
