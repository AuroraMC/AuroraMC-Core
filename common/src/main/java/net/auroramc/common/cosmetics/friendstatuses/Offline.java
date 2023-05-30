/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.common.cosmetics.friendstatuses;

import net.auroramc.api.cosmetics.FriendStatus;

import java.util.Collections;

public class Offline extends FriendStatus {

    public Offline() {
        super(101, "Offline", "Online", UnlockMode.ALL, -1, Collections.emptyList(), Collections.emptyList(), "", "Offline", '7', true, Rarity.COMMON);
    }
}
