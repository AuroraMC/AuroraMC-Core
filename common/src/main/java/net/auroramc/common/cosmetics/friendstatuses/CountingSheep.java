/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.friendstatuses;

import net.auroramc.api.cosmetics.FriendStatus;

import java.util.Collections;

public class CountingSheep extends FriendStatus {

    public CountingSheep() {
        super(118, "&fCounting Sheep", "&fCounting Sheep", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", "&f&lCounting Sheep", 'f', true, Rarity.UNCOMMON);
    }
}
