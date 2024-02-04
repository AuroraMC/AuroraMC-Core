/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.common.cosmetics.friendstatuses;

import net.auroramc.api.cosmetics.FriendStatus;

import java.util.Collections;

public class AFK extends FriendStatus {

    public AFK() {
        super(104, "AFK", "AFK", UnlockMode.ALL, -1, Collections.emptyList(), Collections.emptyList(), "", "Away From Keyboard", '6', true, Rarity.COMMON);
    }
}
