/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.friendstatuses;

import net.auroramc.api.cosmetics.FriendStatus;

import java.util.Collections;

public class BusyHavingALife extends FriendStatus {

    public BusyHavingALife() {
        super(120, "&aBusy having a life", "&aBusy having a life", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", "&a&lBusy having a life", 'a', true, Rarity.UNCOMMON);
    }
}
