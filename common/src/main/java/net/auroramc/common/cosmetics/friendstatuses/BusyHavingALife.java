/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.cosmetics.friendstatuses;

import net.auroramc.api.cosmetics.FriendStatus;

import java.util.Collections;

public class BusyHavingALife extends FriendStatus {

    public BusyHavingALife() {
        super(120, "&aBusy having a life", "&aBusy having a life", UnlockMode.CRATE, -1, Collections.emptyList(), Collections.emptyList(), "Found in Crates", "&a&lBusy having a life", 'a', true, Rarity.UNCOMMON);
    }
}
