/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.friendstatuses;

import net.auroramc.core.api.cosmetics.FriendStatus;

import java.util.Collections;

public class Offline extends FriendStatus {

    public Offline() {
        super(101, "Offline", "Online", UnlockMode.ALL, -1, Collections.emptyList(), Collections.emptyList(), "", "Offline", '7', true);
    }
}
