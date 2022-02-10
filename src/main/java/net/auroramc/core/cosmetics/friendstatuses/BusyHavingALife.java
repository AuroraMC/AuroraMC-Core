/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.friendstatuses;

import net.auroramc.core.api.cosmetics.FriendStatus;

import java.util.Collections;

public class BusyHavingALife extends FriendStatus {

    public BusyHavingALife() {
        super(120, "&aBusy having a life", "&aBusy having a life", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", "&a&lBusy having a life", 'a', true, Rarity.COMMON);
    }
}
