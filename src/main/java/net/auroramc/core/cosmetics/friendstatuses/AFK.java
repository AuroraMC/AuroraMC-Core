/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.cosmetics.friendstatuses;

import net.auroramc.core.api.cosmetics.FriendStatus;

import java.util.Collections;

public class AFK extends FriendStatus {

    public AFK() {
        super(104, "AFK", "AFK", UnlockMode.ALL, -1, Collections.emptyList(), Collections.emptyList(), "", "Away From Keyboard", '6', true, Rarity.COMMON);
    }
}
